package com.hari.tmdb.db.internal.daos

import androidx.room.Dao
import androidx.room.Query
import com.hari.tmdb.db.internal.entity.GenreEntityImp
import kotlinx.coroutines.flow.Flow

@Dao
abstract class GenreDao : EntityDao<GenreEntityImp>() {
    @Query("SELECT * FROM genre")
    abstract fun movieGenre(): Flow<List<GenreEntityImp>>

    @Query("DELETE FROM genre")
    abstract fun deleteAll()
}