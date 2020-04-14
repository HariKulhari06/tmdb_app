package com.hari.tmdb.model.repository


import com.hari.tmdb.model.Filters
import com.hari.tmdb.model.Genre
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    suspend fun refresh()
    suspend fun getMovieGenres(): Flow<List<Genre>>
    suspend fun getMovieFilters(): Flow<Filters>
    suspend fun refreshPopularContents()
    suspend fun popularContents(page: Int)

/*    suspend fun refreshPopularContents()
    suspend fun refreshNowPlayingContents()
    suspend fun refreshUpcomingContents()
    suspend fun refreshTopRatedContents()
    suspend fun popularContents(page: Int)
    suspend fun nowPlayingContents(page: Int)
    suspend fun upcomingContents(page: Int)
    suspend fun topRatedContents(page: Int)*/
}