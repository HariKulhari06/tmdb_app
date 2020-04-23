package com.hari.tmdb.db.internal.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.hari.tmdb.db.internal.entity.ImageEntityImp
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ImageDao : EntityDao<ImageEntityImp>() {
    @Query("DELETE FROM movie_images WHERE movie_id = :movieId")
    abstract suspend fun deleteForShowId(movieId: Int)

    @Query("SELECT COUNT(*) FROM movie_images WHERE movie_id = :movieId")
    abstract suspend fun imageCountForShowId(movieId: Int): Int

    @Query("SELECT * FROM movie_images WHERE movie_id = :movieId")
    abstract fun getImagesForShowId(movieId: Int): Flow<List<ImageEntityImp>>

    @Query("DELETE FROM movie_images")
    abstract suspend fun deleteAll()

    @Transaction
    open suspend fun saveImages(movieId: Int, images: List<ImageEntityImp>) {
        deleteForShowId(movieId)
        insertOrUpdate(images)
    }

    @Transaction
    open suspend fun saveImagesIfEmpty(movieId: Int, images: List<ImageEntityImp>) {
        if (imageCountForShowId(movieId) <= 0) {
            insertAll(images)
        }
    }
}