package com.hari.tmdb.db.internal.daos

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.hari.tmdb.db.internal.entity.AiringTodayShowEntity
import com.hari.tmdb.db.internal.resultentities.AiringTodayEntryWithShow
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AiringTodayShowDao :
    ShowPaginatedEntryDao<AiringTodayShowEntity, AiringTodayEntryWithShow>() {
    @Transaction
    @Query("SELECT * FROM airing_today_shows ORDER BY page, page_order")
    abstract fun dataSource(): DataSource.Factory<Int, AiringTodayEntryWithShow>

    @Query("DELETE FROM airing_today_shows WHERE page = :page")
    abstract override suspend fun deletePage(page: Int)

    @Query("DELETE FROM airing_today_shows")
    abstract override suspend fun deleteAll()

    @Query("SELECT MAX(page) from airing_today_shows")
    abstract override suspend fun getLastPage(): Int?

    @Query("SELECT * FROM airing_today_shows LIMIT 1")
    abstract fun getLatestAiredShow(): Flow<AiringTodayEntryWithShow>

}