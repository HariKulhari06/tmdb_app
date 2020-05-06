package com.hari.tmdb.model.repository


import com.hari.tmdb.model.Filters
import com.hari.tmdb.model.Genre
import com.hari.tmdb.model.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    suspend fun refreshFilters()

    suspend fun getMovieGenres(): Flow<List<Genre>>
    suspend fun getMovieFilters(): Flow<Filters>

    suspend fun movieContents(id: Int): Flow<Movie>
    suspend fun refreshMovieDetails(id: Int)

    suspend fun refreshPopularContents()
    suspend fun popularContents(page: Int): Flow<List<Movie>>

    suspend fun discoverMovies(filters: Flow<Filters>): Flow<List<Movie>>
}