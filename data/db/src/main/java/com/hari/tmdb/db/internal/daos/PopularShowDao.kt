package com.hari.tmdb.db.internal.daos

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.hari.tmdb.db.internal.entity.PopularShowEntity
import com.hari.tmdb.db.internal.resultentities.PopularEntryWithShow

@Dao
abstract class PopularShowDao : ShowPaginatedEntryDao<PopularShowEntity, PopularEntryWithShow>() {
    @Transaction
    @Query("SELECT * FROM popular_shows ORDER BY page, page_order")
    abstract fun dataSource(): DataSource.Factory<Int, PopularEntryWithShow>

    @Query("DELETE FROM popular_shows WHERE page = :page")
    abstract override suspend fun deletePage(page: Int)

    @Query("DELETE FROM popular_shows")
    abstract override suspend fun deleteAll()

    @Query("SELECT MAX(page) from popular_shows")
    abstract override suspend fun getLastPage(): Int?

}