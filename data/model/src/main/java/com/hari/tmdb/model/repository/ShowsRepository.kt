package com.hari.tmdb.model.repository

import androidx.lifecycle.LiveData
import com.hari.tmdb.model.Listing
import com.hari.tmdb.model.LoadingState
import com.hari.tmdb.model.Show
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface ShowsRepository {
    fun getLatestAiredShow(): Flow<Show>

    fun getShow(id: Int): Flow<Show>

    fun refreshShow(id: Int): Flow<LoadingState>

    suspend fun refreshPopularShows(): LiveData<LoadingState>
    fun popularShowsListing(scope: CoroutineScope): Listing<Show>

    suspend fun refreshTopRatedShows(): LiveData<LoadingState>
    fun topRatedShowsListing(scope: CoroutineScope): Listing<Show>

    suspend fun refreshOnTvShows(): LiveData<LoadingState>
    fun onTvShowsListing(scope: CoroutineScope): Listing<Show>

    suspend fun refreshAiringTodayShows(): LiveData<LoadingState>
    fun airingTodayShowsListing(scope: CoroutineScope): Listing<Show>
}