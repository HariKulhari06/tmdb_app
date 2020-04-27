package com.hari.tmdb.db.internal.entity

import androidx.room.*

@Entity(
    tableName = "videos",
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
data class VideoEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "movie_id") val movieId: Int,
    @ColumnInfo(name = "iso31661") val iso31661: String,
    @ColumnInfo(name = "iso6391") val iso6391: String,
    @ColumnInfo(name = "key") val key: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "site") val site: String,
    @ColumnInfo(name = "size") val size: Int,
    @ColumnInfo(name = "type") val type: String
)