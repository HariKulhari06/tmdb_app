package com.hari.tmdb.db.internal.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "languages")
data class LanguageEntity(
    @PrimaryKey @ColumnInfo(name = "iso_639_1") val iso6391: String,
    @ColumnInfo(name = "english_name") val englishName: String,
    @ColumnInfo(name = "name") val name: String?
)