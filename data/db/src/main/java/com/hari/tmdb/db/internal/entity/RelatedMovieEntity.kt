package com.hari.tmdb.db.internal.entity

import androidx.room.*
import com.hari.tmdb.model.MultipleEntry

@Entity(
    tableName = "related_movies",
    indices = [
        Index(value = ["movie_id"]),
        Index(value = ["other_movie_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = MovieEntityImp::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("movie_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MovieEntityImp::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("other_movie_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RelatedMovieEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override val id: Int = 0,
    @ColumnInfo(name = "movie_id") override val movieId: Int,
    @ColumnInfo(name = "other_movie_id") override val otherMovieId: Int,
    @ColumnInfo(name = "order_index") val orderIndex: Int
) : MultipleEntry
