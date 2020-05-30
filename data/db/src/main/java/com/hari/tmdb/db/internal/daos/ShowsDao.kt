package com.hari.tmdb.db.internal.daos

import androidx.room.Dao
import androidx.room.Query
import com.hari.tmdb.db.internal.entity.ShowEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ShowsDao : EntityDao<ShowEntity>() {
    @Query("SELECT * FROM shows WHERE tmdb_id =:id")
    abstract fun show(id: Int): Flow<ShowEntity>

    @Query("SELECT id FROM shows WHERE tmdb_id =:tmdb")
    abstract suspend fun getShowId(tmdb: Int): Int
}