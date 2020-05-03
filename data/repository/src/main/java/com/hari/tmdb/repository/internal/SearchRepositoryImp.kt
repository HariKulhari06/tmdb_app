@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.hari.tmdb.repository.internal

import com.hari.tmdb.db.internal.MoviesDataBase
import com.hari.tmdb.ext.bodyOrThrow
import com.hari.tmdb.model.Movie
import com.hari.tmdb.model.mapper.Mapper
import com.hari.tmdb.model.repository.SearchRepository
import com.uwetrottmann.tmdb2.entities.BaseMovie
import com.uwetrottmann.tmdb2.entities.MovieResultsPage
import com.uwetrottmann.tmdb2.services.SearchService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchRepositoryImp @Inject constructor(
    private val searchService: SearchService,
    private val moviesDataBase: MoviesDataBase
) : SearchRepository {

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


    override suspend fun search(query: Flow<String>): Flow<List<Movie>> {
        return query
            .debounce(300)
            .distinctUntilChanged()
            .map { searchQuery ->
                val list = if (searchQuery.isBlank())
                    emptyList()
                else {
                    mapper.map(
                        searchService
                            .movie(searchQuery, 1, null, null, false, 0, 0)
                            .execute()
                            .bodyOrThrow()
                    )
                }
                list
            }

    }
}