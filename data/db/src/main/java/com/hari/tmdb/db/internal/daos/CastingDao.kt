package com.hari.tmdb.db.internal.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.hari.tmdb.db.internal.entity.CastingEntityImp
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CastingDao : EntityDao<CastingEntityImp>() {
    @Query("DELETE FROM movie_casting WHERE movie_id = :movieId")
    abstract suspend fun deleteForShowId(movieId: Int)

    @Query("SELECT COUNT(*) FROM movie_casting WHERE movie_id = :movieId")
    abstract suspend fun castingCountForShowId(movieId: Int): Int

    @Query("SELECT * FROM movie_casting WHERE movie_id = :movieId")
    abstract fun getCastingForShowId(movieId: Int): Flow<List<CastingEntityImp>>

    @Query("DELETE FROM movie_casting")
    abstract suspend fun deleteAll()

    @Transaction
    open suspend fun saveCasting(movieId: Int, casting: List<CastingEntityImp>) {
        deleteForShowId(movieId)
        insertAll(casting)
    }

    @Transaction
    open suspend fun saveCastingIfEmpty(movieId: Int, casting: List<CastingEntityImp>) {
        if (castingCountForShowId(movieId) <= 0) {
            insertAll(casting)
        }
    }
}