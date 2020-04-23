package com.hari.tmdb.db.internal.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.hari.tmdb.db.internal.entity.VideoEntity

@Dao
abstract class VideoDao : BaseDao<VideoEntity>() {
    @Query("DELETE FROM videos WHERE movie_id = :movieId")
    abstract suspend fun deleteForShowId(movieId: Int)

    @Transaction
    open suspend fun saveVideos(movieId: Int, videos: List<VideoEntity>) {
        deleteForShowId(movieId)
        insertAll(videos)
        // insertOrUpdate(videos)
    }

    suspend fun insertOrUpdate(entity: VideoEntity): Long {
        return if (entity.id.isNullOrEmpty()) {
            insert(entity)
        } else {
            update(entity)
            entity.id.toLong()
        }
    }

    @Transaction
    open suspend fun insertOrUpdate(entities: List<VideoEntity>) {
        entities.forEach {
            insertOrUpdate(it)
        }
    }
}