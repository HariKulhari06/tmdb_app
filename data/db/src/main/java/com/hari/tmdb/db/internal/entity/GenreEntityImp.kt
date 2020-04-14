package com.hari.tmdb.db.internal.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hari.tmdb.db.internal.entity.GenreEntity

@Entity(tableName = "genre")
data class GenreEntityImp(
    @PrimaryKey override var id: Int,
    override var name: String
) : GenreEntity