package com.hari.tmdb.model

import androidx.annotation.IdRes


enum class Gender(val type: Int) {
    MALE(1),
    FEMALE(2);

    companion object {
        fun gePage(@IdRes id: Int): Gender {
            return values()
                .first { it.type == id }
        }
    }
}