package com.hari.tmdb.db.internal.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hari.tmdb.db.internal.entity.helper.ImageEntity


@Entity(tableName = "backdrop_image")
data class BackdropImageEntity(
    @PrimaryKey(autoGenerate = true) override val id: Int = 0,
    @ColumnInfo(name = "movie_id") override var movieId: Int? = null,
    @ColumnInfo(name = "file_path") override var filePath: String? = null,
    @ColumnInfo(name = "width") override var width: Int? = null,
    @ColumnInfo(name = "height") override var height: Int? = null,
    @ColumnInfo(name = "iso_639_1") override var iso6391: String? = null,
    @ColumnInfo(name = "aspect_ratio") override var aspectRatio: Double? = null,
    @ColumnInfo(name = "vot_average") override var voteAverage: Double? = null,
    @ColumnInfo(name = "vote_count") override var voteCount: Int? = null
) : ImageEntity