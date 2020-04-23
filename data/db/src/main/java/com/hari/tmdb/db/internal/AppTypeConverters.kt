package com.hari.tmdb.db.internal

import androidx.room.TypeConverter
import com.hari.tmdb.model.ImageType

object AppTypeConverters {
    private val imageTypeValues by lazy(LazyThreadSafetyMode.NONE) { ImageType.values() }

    @TypeConverter
    @JvmStatic
    fun fromImageType(value: ImageType) = value.storageKey

    @TypeConverter
    @JvmStatic
    fun toImageType(value: String?) = imageTypeValues.firstOrNull { it.storageKey == value }
}