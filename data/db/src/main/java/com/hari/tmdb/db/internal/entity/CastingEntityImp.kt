package com.hari.tmdb.db.internal.entity

import androidx.room.*
import com.hari.tmdb.db.internal.entity.helper.CastingHelper

@Entity(
    tableName = "movie_casting",
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
data class CastingEntityImp(
    @PrimaryKey @ColumnInfo(name = "id") override val id: Int,
    @ColumnInfo(name = "movie_id") val movieId: Int,
    @ColumnInfo(name = "cast_id") override val castId: Int,
    @ColumnInfo(name = "character") override val character: String,
    @ColumnInfo(name = "credit_id") override val creditId: String,
    @ColumnInfo(name = "gender") override val gender: Int? = null,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "order") override val order: Int,
    @ColumnInfo(name = "profile_path") override val profilePath: String? = null
) : CastingHelper