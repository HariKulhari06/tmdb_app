@file:Suppress(
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)

package com.hari.tmdb.db.internal.mapper

import com.hari.tmdb.db.internal.entity.VideoEntity
import com.hari.tmdb.model.Video
import com.hari.tmdb.model.mapper.Mapper
import com.uwetrottmann.tmdb2.entities.Movie

val tmdbMoviesToVideoEntity = object : Mapper<Movie, List<VideoEntity>> {
    override suspend fun map(from: Movie): List<VideoEntity> {
        return from.videos.results.map { video ->
            VideoEntity(
                id = video.id,
                movieId = from.id,
                name = video.name,
                key = video.key,
                iso6391 = video.iso_639_1,
                iso31661 = video.iso_3166_1,
                type = video.type?.name ?: "",
                site = video.site,
                size = video.size
            )
        }
    }

}

val videoEntityToVideo = object : Mapper<VideoEntity, Video> {
    override suspend fun map(from: VideoEntity): Video {
        return Video(
            id = from.id,
            name = from.name,
            size = from.size,
            site = from.site,
            type = from.type,
            iso31661 = from.iso31661,
            iso6391 = from.iso6391,
            key = from.key
        )
    }

}