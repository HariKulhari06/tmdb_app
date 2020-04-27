package com.hari.tmdb.db.internal.entity

import androidx.room.*
import com.hari.tmdb.db.internal.entity.helper.ImageEntity
import com.hari.tmdb.model.ImageType


@Entity(
    tableName = "movie_images",
    indices = [
        Index(value = ["movie_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = MovieEntityImp::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("movie_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ImageEntityImp(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Int = 0,
    @ColumnInfo(name = "movie_id") override var movieId: Int,
    @ColumnInfo(name = "file_path") override var filePath: String? = null,
    @ColumnInfo(name = "width") override var width: Int? = null,
    @ColumnInfo(name = "height") override var height: Int? = null,
    @ColumnInfo(name = "iso_639_1") override var iso6391: String? = null,
    @ColumnInfo(name = "aspect_ratio") override var aspectRatio: Double? = null,
    @ColumnInfo(name = "vot_average") override var voteAverage: Double? = null,
    @ColumnInfo(name = "vote_count") override var voteCount: Int? = null,
    @ColumnInfo(name = "type") override var type: ImageType
) : ImageEntity