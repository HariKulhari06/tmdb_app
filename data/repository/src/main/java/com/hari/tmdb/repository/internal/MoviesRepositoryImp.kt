package com.hari.tmdb.repository.internal

import com.hari.tmdb.db.internal.MoviesDataBase
import com.hari.tmdb.ext.bodyOrThrow
import com.hari.tmdb.ext.executeWithRetry
import com.hari.tmdb.ext.toResult
import com.hari.tmdb.log.Timber
import com.hari.tmdb.model.*
import com.hari.tmdb.model.mapper.Mapper
import com.hari.tmdb.model.repository.MoviesRepository
import com.uwetrottmann.tmdb2.entities.AppendToResponse
import com.uwetrottmann.tmdb2.entities.BaseMovie
import com.uwetrottmann.tmdb2.entities.DiscoverFilter
import com.uwetrottmann.tmdb2.entities.MovieResultsPage
import com.uwetrottmann.tmdb2.enumerations.AppendToResponseItem
import com.uwetrottmann.tmdb2.enumerations.SortBy
import com.uwetrottmann.tmdb2.services.DiscoverService
import com.uwetrottmann.tmdb2.services.GenresService
import com.uwetrottmann.tmdb2.services.MoviesService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import retrofit2.Call
import javax.inject.Inject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MoviesRepositoryImp @Inject constructor(
    private val moviesDataBase: MoviesDataBase,
    private val genresService: GenresService,
    private val moviesService: MoviesService,
    private val discoverService: DiscoverService
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
        val languageFlow = moviesDataBase.languages()
        val adultFlow = adultsFiltersAsFlow()

        return combine(genreFlow, adultFlow, languageFlow) { genre, adults, languages ->
            val genres = genre
                .sortedBy { it.name }
                .toSet()

            Filters(
                genres = genres,
                includeAdult = adults.toSet(),
                sortBy = getSortingOptionAsSet(),
                certifications = setOfCertifications,
                languages = languages.toSet()
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

    override suspend fun discoverMovies(filters: Flow<Filters>): Flow<List<Movie>> {

        val baseMovieToMovieMapper = object : Mapper<BaseMovie, Movie> {
            override suspend fun map(from: BaseMovie): Movie = Movie(
                id = from.id,
                adult = from.adult,
                backdropPath = from.backdrop_path,
                originalLanguage = from.original_language,
                originalTitle = from.original_title,
                overview = from.overview,
                popularity = from.popularity,
                posterPath = from.poster_path,
                releaseDate = "",
                title = from.title,
                video = false,
                voteAverage = from.vote_average,
                voteCount = from.vote_count
            )
        }

        val mapper = object : Mapper<MovieResultsPage, List<Movie>> {
            override suspend fun map(from: MovieResultsPage): List<Movie> {
                return from.results?.map { baseMovie ->
                    baseMovieToMovieMapper.map(baseMovie)
                } ?: emptyList()
            }
        }


        return filters
            .map { filter ->
                mapper.map(
                    discoverService.tmdbDiscover(filter)
                        .execute()
                        .bodyOrThrow()
                )
            }
    }

    override suspend fun refreshMoviesByCategory(movieCategory: MovieCategory) {
        try {
            val result = when (movieCategory) {
                MovieCategory.POPULAR -> {
                    moviesService.popular(1, null, null)
                }
                MovieCategory.NOW_PLAYING -> {
                    moviesService.nowPlaying(1, null, null)
                }
                MovieCategory.UPCOMING -> {
                    moviesService.upcoming(1, null, null)
                }
                MovieCategory.TOP_RATED -> {
                    moviesService.topRated(1, null, null)
                }
                MovieCategory.OTHER -> {
                    moviesService.popular(1, null, null)
                }
            }.executeWithRetry().toResult()

            when (result) {
                is Success -> {
                    moviesDataBase.saveMovies(result.data, movieCategory)
                }
                is ErrorResult -> {
                    Timber.error(result.throwable)
                }
            }
        } catch (e: Exception) {
            Timber.error(e)
        }
    }

    override suspend fun getMovies(movieCategory: MovieCategory): Flow<List<Movie>> =
        moviesDataBase.movies(movieCategory)

}

private fun DiscoverService.tmdbDiscover(filters: Filters): Call<MovieResultsPage> {
    val language = filters.languages.firstOrNull()?.iso6391
    val certificate = filters.certifications.firstOrNull()?.name
    val adult = filters.includeAdult.firstOrNull().equals("Yes")
    val sortBy = SortBy.values().firstOrNull { sort: SortBy ->
        val name = filters.sortBy.firstOrNull()?.name
        if (name.isNullOrEmpty())
            false
        else
            name == sort.name
    }
    val genres = filters.genres.let { genre: Set<Genre> ->
        if (genre.isEmpty()) {
            null
        } else {
            DiscoverFilter(genre.first().id)
        }
    }

    return discoverMovie(
        language,
        null,
        sortBy,
        null,
        certificate,
        null,
        adult,
        null,
        1,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        genres,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )
}





