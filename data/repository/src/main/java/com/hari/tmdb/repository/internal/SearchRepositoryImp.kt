@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.hari.tmdb.repository.internal

import com.hari.tmdb.db.internal.MoviesDataBase
import com.hari.tmdb.ext.bodyOrThrow
import com.hari.tmdb.model.Keyword
import com.hari.tmdb.model.Movie
import com.hari.tmdb.model.mapper.Mapper
import com.hari.tmdb.model.mapper.forLists
import com.hari.tmdb.model.repository.SearchRepository
import com.uwetrottmann.tmdb2.entities.BaseKeyword
import com.uwetrottmann.tmdb2.entities.BaseMovie
import com.uwetrottmann.tmdb2.entities.MovieResultsPage
import com.uwetrottmann.tmdb2.services.SearchService
import kotlinx.coroutines.flow.*
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
            .flatMapLatest { searchQuery ->
                flow {
                    emit(
                        if (searchQuery.isEmpty()) {
                            emptyList()
                        } else {
                            mapper.map(
                                searchService
                                    .movie(searchQuery, 1, null, null, false, 0, 0)
                                    .execute()
                                    .bodyOrThrow()
                            )
                        }
                    )

                }
            }
    }


    val baseKeywordToKeyword = object : Mapper<BaseKeyword, Keyword> {
        override suspend fun map(from: BaseKeyword): Keyword {
            return Keyword(
                id = from.id,
                name = from.name
            )
        }

    }

    override suspend fun keywords(query: Flow<String>): Flow<List<Keyword>> {
        return query
            .debounce(300)
            .distinctUntilChanged()
            .flatMapLatest { searchQuery ->
                flow {
                    emit(
                        baseKeywordToKeyword.forLists()
                            .invoke(
                                searchService
                                    .keyword(searchQuery, 1)
                                    .execute()
                                    .bodyOrThrow().results
                            )
                    )
                }
            }
    }
}