package com.hari.tmdb.db.internal.daos

import androidx.room.Dao
import androidx.room.Query
import com.hari.tmdb.db.internal.entity.PopularMovieEntityImp
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PopularMovieDao : EntityDao<PopularMovieEntityImp>() {
    @Query("SELECT * FROM popular_movie")
    abstract fun movieGenre(): Flow<List<PopularMovieEntityImp>>

    @Query("DELETE FROM popular_movie")
    abstract fun deleteAll()
}