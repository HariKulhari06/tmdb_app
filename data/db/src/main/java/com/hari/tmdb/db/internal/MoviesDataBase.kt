package com.hari.tmdb.db.internal

import com.hari.tmdb.model.Genre
import com.hari.tmdb.model.Language
import com.hari.tmdb.model.Movie
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

    suspend fun popularMovies(): Flow<List<Movie>>
}