package com.hari.tmdb.repository.internal

import com.hari.tmdb.db.internal.MoviesDataBase
import com.hari.tmdb.ext.executeWithRetry
import com.hari.tmdb.ext.toResult
import com.hari.tmdb.log.Timber
import com.hari.tmdb.model.*
import com.hari.tmdb.model.repository.MoviesRepository
import com.uwetrottmann.tmdb2.entities.AppendToResponse
import com.uwetrottmann.tmdb2.enumerations.AppendToResponseItem
import com.uwetrottmann.tmdb2.services.GenresService
import com.uwetrottmann.tmdb2.services.MoviesService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class MoviesRepositoryImp @Inject constructor(
    private val moviesDataBase: MoviesDataBase,
    private val genresService: GenresService,
    private val moviesService: MoviesService
) : MoviesRepository {

    override suspend fun refreshFilters() {
        try {
            val result = genresService.movie(null)
                .executeWithRetry()
                .toResult()

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

    override suspend fun getMovieGenres(): Flow<List<Genre>> {
        return moviesDataBase.moviesGenre()
    }

    override suspend fun getMovieFilters(): Flow<Filters> {
        val genreFlow = moviesDataBase.moviesGenre()
        val adultFlow = flowOf(listOf(true, false))

        return combine(genreFlow, adultFlow) { genre, adults ->
            val genres = genre
                .sortedBy { it.name }
                .toSet()

            Filters(
                genres = genres,
                includeAdult = adults.toSet(),
                sortBy = getSortingOptionAsSet()
            )
        }
    }

    override suspend fun movieContents(id: Int): Flow<Movie> {
        return moviesDataBase.movie(id)
    }

    override suspend fun refreshMovieDetails(id: Int) {
        try {
            val result: Result<com.uwetrottmann.tmdb2.entities.Movie> = moviesService.summary(
                id,
                null,
                AppendToResponse(
                    AppendToResponseItem.CREDITS,
                    AppendToResponseItem.IMAGES,
                    AppendToResponseItem.VIDEOS,
                    AppendToResponseItem.SIMILAR
                )
            ).executeWithRetry()
                .toResult()

            when (result) {
                is Success -> {
                    moviesDataBase.saveMovie(result.data)
                }
                is ErrorResult -> {
                    Timber.error(result.throwable)
                }
            }
        } catch (e: Exception) {
            Timber.error(e)
        }

    }

    override suspend fun refreshPopularContents() {
        try {
            val result = moviesService.popular(1, null, null)
                .executeWithRetry()
                .toResult()

            when (result) {
                is Success -> {
                    moviesDataBase.savePopularMovies(result.data)
                }
                is ErrorResult -> {
                    Timber.error(result.throwable)
                }
            }

        } catch (e: Exception) {
            Timber.error(e)
        }
    }

    override suspend fun popularContents(page: Int): Flow<List<Movie>> {
        return moviesDataBase.popularMovies()
    }
}



