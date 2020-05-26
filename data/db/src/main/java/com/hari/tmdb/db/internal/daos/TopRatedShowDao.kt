package com.hari.tmdb.db.internal.daos

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.hari.tmdb.db.internal.entity.TopRatedShowEntity
import com.hari.tmdb.db.internal.resultentities.TopRatedEntryWithShow

@Dao
abstract class TopRatedShowDao :
    ShowPaginatedEntryDao<TopRatedShowEntity, TopRatedEntryWithShow>() {
    @Transaction
    @Query("SELECT * FROM top_rated_shows ORDER BY page, page_order")
    abstract fun dataSource(): DataSource.Factory<Int, TopRatedEntryWithShow>

    @Query("DELETE FROM top_rated_shows WHERE page = :page")
    abstract override suspend fun deletePage(page: Int)

    @Query("DELETE FROM top_rated_shows")
    abstract override suspend fun deleteAll()

    @Query("SELECT MAX(page) from top_rated_shows")
    abstract override suspend fun getLastPage(): Int?

}