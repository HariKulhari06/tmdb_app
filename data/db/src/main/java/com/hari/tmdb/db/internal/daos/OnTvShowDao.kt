package com.hari.tmdb.db.internal.daos

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.hari.tmdb.db.internal.entity.OnTvShowEntity
import com.hari.tmdb.db.internal.resultentities.OnTvEntryWithShow

@Dao
abstract class OnTvShowDao : ShowPaginatedEntryDao<OnTvShowEntity, OnTvEntryWithShow>() {
    @Transaction
    @Query("SELECT * FROM on_tv_shows ORDER BY page, page_order")
    abstract fun dataSource(): DataSource.Factory<Int, OnTvEntryWithShow>

    @Query("DELETE FROM on_tv_shows WHERE page = :page")
    abstract override suspend fun deletePage(page: Int)

    @Query("DELETE FROM on_tv_shows")
    abstract override suspend fun deleteAll()

    @Query("SELECT MAX(page) from on_tv_shows")
    abstract override suspend fun getLastPage(): Int?

}