@file:Suppress(
    "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)

package com.hari.tmdb.db.internal.mapper

import com.hari.tmdb.db.internal.entity.ImageEntityImp
import com.hari.tmdb.model.ImageType
import com.hari.tmdb.model.mapper.Mapper
import com.uwetrottmann.tmdb2.entities.Image
import com.uwetrottmann.tmdb2.entities.Movie

val tmdbMovieToImageEntityImp = object : Mapper<Movie, List<ImageEntityImp>> {
    override suspend fun map(from: Movie): List<ImageEntityImp> {
        fun mapImage(image: Image, type: ImageType): ImageEntityImp {
            return ImageEntityImp(
                movieId = from.id,
                aspectRatio = image.aspect_ratio,
                filePath = image.file_path,
                voteCount = image.vote_count,
                voteAverage = image.vote_average,
                height = image.height,
                iso6391 = image.iso_639_1,
                type = type,
                width = image.width
            )
        }

        val result = mutableListOf<ImageEntityImp>()
        from.images.posters.mapTo(result) { mapImage(it, ImageType.POSTER) }
        from.images.backdrops.mapTo(result) { mapImage(it, ImageType.BACKDROP) }

        return result
    }

}

