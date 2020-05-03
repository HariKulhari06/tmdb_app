package com.hari.tmdb.db.internal.daos

import androidx.room.Dao
import androidx.room.Query
import com.hari.tmdb.db.internal.entity.LanguageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LanguageDao {
    @Query("SELECT * FROM languages")
    fun languages(): Flow<List<LanguageEntity>>
}