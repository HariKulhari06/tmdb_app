package com.hari.tmdb.repository.internal

import com.hari.tmdb.db.internal.MoviesDataBase
import com.hari.tmdb.ext.executeWithRetry
import com.hari.tmdb.ext.toResult
import com.hari.tmdb.log.Timber
import com.hari.tmdb.model.*
import com.hari.tmdb.model.mapper.toLambda
import com.hari.tmdb.model.repository.MoviesRepository
import com.hari.tmdb.repository.internal.mapper.TmdbApiGenreResultsToTmdbGenre
import com.hari.tmdb.repository.internal.mapper.toGenre
import com.hari.tmdb.repository.internal.mapper.toMovies
import com.uwetrottmann.tmdb2.DiscoverMovieBuilder
import com.uwetrottmann.tmdb2.entities.MovieResultsPage
import com.uwetrottmann.tmdb2.services.GenresService
import com.uwetrottmann.tmdb2.services.MoviesService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class MoviesRepositoryImp @Inject constructor(
    private val moviesDataBase: MoviesDataBase,
    private val genresService: GenresService,
    private val moviesService: MoviesService,
    private val discoverMovieBuilder: DiscoverMovieBuilder,
    private val mapper: TmdbApiGenreResultsToTmdbGenre
) : MoviesRepository {
    override suspend fun refresh() {
        try {
            val result = genresService.movie(null)
                .executeWithRetry()
                .toResult(mapper.toLambda())

            when (result) {
                is Success -> {
                    moviesDataBase.saveMovieGenre(result.data)
                }
                is ErrorResult -> {
                    Timber.error(result.throwable)
                }
            }
        } catch (e: Exception) {
            Timber.error(e)
        }
    }

    override suspend fun getMovieGenres(): Flow<List<Genre>> = flow {
        moviesDataBase.moviesGenre().collect { genreEntities ->
            emit(genreEntities.map { it.toGenre() })
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun getMovieFilters(): Flow<Filters> {
        val genreFlow = moviesDataBase.moviesGenre()
        val adultFlow = flowOf(listOf(true, false))

        return combine(genreFlow, adultFlow) { genreEntities, adults ->
            val genres = genreEntities
                .map { it.toGenre() }
                .sortedBy { it.name }
                .toSet()

            Filters(
                genres = genres,
                includeAdult = adults.toSet(),
                sortBy = getSortingOptionAsSet()
            )
        }
    }

    override suspend fun refreshPopularContents() {
        try {
            val result: Result<MovieResultsPage> = moviesService.popular(1, null, null)
                .executeWithRetry()
                .toResult()

            when (result) {
                is Success -> {
                    moviesDataBase.savePopularMovies(result.data.toMovies())
                }
                is ErrorResult -> {
                    Timber.error(result.throwable)
                }
            }

        } catch (e: Exception) {
            Timber.error(e)
        }
    }

    override suspend fun popularContents(page: Int) {

    }


}



