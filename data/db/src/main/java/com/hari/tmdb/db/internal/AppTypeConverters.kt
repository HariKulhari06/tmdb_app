package com.hari.tmdb.db.internal

import androidx.room.TypeConverter
import com.hari.tmdb.model.ImageType
import com.hari.tmdb.model.MovieCategory
import com.hari.tmdb.model.ShowStatus

object AppTypeConverters {
    private val imageTypeValues by lazy(LazyThreadSafetyMode.NONE) { ImageType.values() }
    private val movieCategoryTypeValues by lazy(LazyThreadSafetyMode.NONE) { MovieCategory.values() }
    private val showStatusTypeValues by lazy(LazyThreadSafetyMode.NONE) { ShowStatus.values() }

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


    @TypeConverter
    @JvmStatic
    fun fromShowsStatus(value: ShowStatus) = value.storageKey

    @TypeConverter
    @JvmStatic
    fun toShowsStatus(value: String?) = showStatusTypeValues.firstOrNull { it.storageKey == value }
}