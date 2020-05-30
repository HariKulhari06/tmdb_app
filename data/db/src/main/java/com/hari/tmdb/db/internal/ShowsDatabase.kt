package com.hari.tmdb.db.internal

import androidx.paging.DataSource
import com.hari.tmdb.db.internal.entity.PopularShowEntity
import com.hari.tmdb.db.internal.entity.ShowEntity
import com.hari.tmdb.model.Show
import kotlinx.coroutines.flow.Flow

interface ShowsDatabase {
    fun getLatestAiredShow(): Flow<Show>

    fun getShow(id: Int): Flow<Show>

    suspend fun getShowId(tmdb: Int): Int

    suspend fun insertShow(data: ShowEntity)

    suspend fun insertPopularShows(shows: List<Pair<ShowEntity, PopularShowEntity>>)
    suspend fun getPopularShowLastPage(): Int
    fun popularShowDataSource(): DataSource.Factory<Int, Show>

    suspend fun insertTopRatedShows(shows: List<Pair<ShowEntity, PopularShowEntity>>)
    suspend fun getTopRatedLastPage(): Int
    fun topRatedShowDataSource(): DataSource.Factory<Int, Show>

    suspend fun insertOnTvShows(shows: List<Pair<ShowEntity, PopularShowEntity>>)
    suspend fun getOnTvLastPage(): Int
    fun onTvShowDataSource(): DataSource.Factory<Int, Show>

    suspend fun insertAiringTodayShows(shows: List<Pair<ShowEntity, PopularShowEntity>>)
    suspend fun getAiringTodayLastPage(): Int
    fun airingTodayShowDataSource(): DataSource.Factory<Int, Show>
}