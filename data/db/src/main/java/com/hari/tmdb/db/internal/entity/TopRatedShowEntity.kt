package com.hari.tmdb.db.internal.entity

import androidx.room.*
import com.hari.tmdb.model.PaginatedEntry


@Entity(
    tableName = "top_rated_shows",
    indices = [
        Index(value = ["show_id"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(
            entity = ShowEntity::class,
            parentColumns = arrayOf("tmdb_id"),
            childColumns = arrayOf("show_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TopRatedShowEntity(
    @PrimaryKey(autoGenerate = true) override val id: Int = 0,
    @ColumnInfo(name = "show_id") override val movieId: Int,
    @ColumnInfo(name = "page") override val page: Int,
    @ColumnInfo(name = "page_order") val pageOrder: Int
) : PaginatedEntry