package com.hari.tmdb.db.internal

import androidx.paging.DataSource
import com.hari.tmdb.model.Genre
import com.hari.tmdb.model.Language
import com.hari.tmdb.model.Movie
import com.hari.tmdb.model.MovieCategory
import com.uwetrottmann.tmdb2.entities.GenreResults
import com.uwetrottmann.tmdb2.entities.MovieResultsPage
import kotlinx.coroutines.flow.Flow
import com.uwetrottmann.tmdb2.entities.Movie as TmdbMovie

interface MoviesDataBase {
    suspend fun moviesGenre(): Flow<List<Genre>>

    suspend fun languages(): Flow<List<Language>>

    suspend fun saveMovieGenre(result: GenreResults)

    suspend fun movie(id: Int): Flow<Movie>

    suspend fun saveMovie(movie: TmdbMovie)

    suspend fun savePopularMovies(result: MovieResultsPage)

    suspend fun saveMovies(result: MovieResultsPage, movieCategory: MovieCategory)

    suspend fun popularMovies(): Flow<List<Movie>>
    suspend fun movies(movieCategory: MovieCategory): Flow<List<Movie>>
    fun moviesDataSource(movieCategory: MovieCategory): DataSource.Factory<Int, Movie>
    suspend fun getMovieLastPage(id: Int, movieCategory: MovieCategory): Int
}