package com.hari.tmdb.model.repository

import androidx.lifecycle.LiveData
import com.hari.tmdb.model.Listing
import com.hari.tmdb.model.LoadingState
import com.hari.tmdb.model.Show
import kotlinx.coroutines.CoroutineScope

interface ShowsRepository {
    suspend fun refreshPopularShows(): LiveData<LoadingState>
    fun popularShowsListing(scope: CoroutineScope): Listing<Show>
}