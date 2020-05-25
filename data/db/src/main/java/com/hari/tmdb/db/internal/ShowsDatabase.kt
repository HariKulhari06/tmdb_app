package com.hari.tmdb.db.internal

import androidx.paging.DataSource
import com.hari.tmdb.db.internal.entity.PopularShowEntity
import com.hari.tmdb.db.internal.entity.ShowEntity
import com.hari.tmdb.model.Show

interface ShowsDatabase {
    suspend fun insertPopularShows(shows: List<Pair<ShowEntity, PopularShowEntity>>)
    suspend fun getPopularShowLastPage(): Int
    fun popularShowDataSource(): DataSource.Factory<Int, Show>
}