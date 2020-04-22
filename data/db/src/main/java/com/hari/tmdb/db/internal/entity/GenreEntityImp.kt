package com.hari.tmdb.db.internal.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hari.tmdb.db.internal.entity.helper.GenreEntity

@Entity(tableName = "genre")
data class GenreEntityImp(
    @ColumnInfo(name = "id") @PrimaryKey override var id: Int = 0,
    @ColumnInfo(name = "name") override var name: String? = null
) : GenreEntity