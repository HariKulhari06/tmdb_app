package com.hari.tmdb.db.internal.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.hari.tmdb.db.internal.entity.PopularMovieEntity
import com.hari.tmdb.db.internal.resultentities.PopularEntryWithMovie
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PopularMovieDao : PaginatedEntryDao<PopularMovieEntity, PopularEntryWithMovie>() {

    @Transaction
    @Query("SELECT * FROM popular_movies WHERE page = :page ORDER BY page_order")
    abstract fun entriesObservable(page: Int): Flow<List<PopularMovieEntity>>

    @Transaction
    @Query("SELECT * FROM popular_movies ORDER BY page, page_order")
    abstract fun entriesObservable(): Flow<List<PopularEntryWithMovie>>

    @Query("DELETE FROM popular_movies WHERE page = :page")
    abstract override suspend fun deletePage(page: Int)

    @Query("DELETE FROM popular_movies")
    abstract override suspend fun deleteAll()

    @Query("SELECT MAX(page) from popular_movies")
    abstract override suspend fun getLastPage(): Int?
}