package com.hari.tmdb.db.internal.entity

import androidx.room.*
import com.hari.tmdb.model.PaginatedEntry


@Entity(
    tableName = "popular_movies",
    indices = [
        Index(value = ["movie_id"], unique = true)
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
data class PopularMovieEntity(
    @PrimaryKey(autoGenerate = true) override val id: Int = 0,
    @ColumnInfo(name = "movie_id") override val movieId: Int,
    @ColumnInfo(name = "page") override val page: Int,
    @ColumnInfo(name = "page_order") val pageOrder: Int
) : PaginatedEntry