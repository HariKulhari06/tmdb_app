package com.hari.tmdb.db.internal

import com.hari.tmdb.db.internal.entity.GenreEntity
import com.hari.tmdb.model.Genre
import com.hari.tmdb.model.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesDataBase {
    suspend fun moviesGenre(): Flow<List<GenreEntity>>
    suspend fun saveMovieGenre(genres: List<Genre>)
    suspend fun savePopularMovies(movies: List<Movie>)
}