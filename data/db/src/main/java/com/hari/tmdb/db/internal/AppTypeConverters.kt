package com.hari.tmdb.db.internal

import androidx.room.TypeConverter
import com.hari.tmdb.model.ImageType
import com.hari.tmdb.model.MovieCategory

object AppTypeConverters {
    private val imageTypeValues by lazy(LazyThreadSafetyMode.NONE) { ImageType.values() }
    private val movieCategoryTypeValues by lazy(LazyThreadSafetyMode.NONE) { MovieCategory.values() }

    @TypeConverter
    @JvmStatic
    fun fromImageType(value: ImageType) = value.storageKey

    @TypeConverter
    @JvmStatic
    fun toImageType(value: String?) = imageTypeValues.firstOrNull { it.storageKey == value }


    @TypeConverter
    @JvmStatic
    fun fromMovieCategory(value: MovieCategory) = value.position

    @TypeConverter
    @JvmStatic
    fun toMovieCategory(value: Int?) = movieCategoryTypeValues.firstOrNull { it.position == value }
}