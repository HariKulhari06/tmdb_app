package com.hari.tmdb.model.repository


import androidx.lifecycle.LiveData
import com.hari.tmdb.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    suspend fun refreshFilters()

    suspend fun getMovieGenres(): Flow<List<Genre>>

    suspend fun getMovieFilters(): Flow<Filters>

    suspend fun movieContents(id: Int): Flow<Movie>

    suspend fun refreshMovieDetails(id: Int)

    suspend fun discoverMovies(filters: Flow<Filters>): Flow<List<Movie>>

    suspend fun refreshMoviesByCategory(movieCategory: MovieCategory): LiveData<LoadingState>

    fun moviesPagedList(
        movieCategory: MovieCategory,
        scope: CoroutineScope
    ): Listing<Movie>
}