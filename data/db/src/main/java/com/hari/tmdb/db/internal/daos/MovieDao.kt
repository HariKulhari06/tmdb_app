package com.hari.tmdb.db.internal.daos

import androidx.room.Dao
import androidx.room.Query
import com.hari.tmdb.db.internal.entity.MovieEntityImp
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MovieDao : EntityDao<MovieEntityImp>() {
    @Query("SELECT * FROM movie ORDER BY popularity DESC")
    abstract fun movies(): Flow<List<MovieEntityImp>>

    @Query("SELECT * FROM movie WHERE id =:id")
    abstract fun movie(id: Int): Flow<MovieEntityImp>

    @Query("DELETE FROM movie")
    abstract fun deleteAll()
}