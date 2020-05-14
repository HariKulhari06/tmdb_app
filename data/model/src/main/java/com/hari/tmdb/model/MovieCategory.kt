package com.hari.tmdb.model

import android.os.Parcelable
import androidx.annotation.IdRes
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class MovieCategory(val position: Int) : Parcelable {
    POPULAR(1),
    NOW_PLAYING(2),
    UPCOMING(3),
    TOP_RATED(4),
    OTHER(0);

    companion object {
        fun movieCategory(@IdRes id: Int): MovieCategory {
            return MovieCategory
                .values()
                .firstOrNull { it.position == id } ?: OTHER
        }
    }
}