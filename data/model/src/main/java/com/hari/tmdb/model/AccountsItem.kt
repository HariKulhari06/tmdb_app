package com.hari.tmdb.model

import android.graphics.drawable.Drawable

data class AccountsItem(
    val icon: Drawable?,
    val title: String,
    val showArrow: Boolean = true
)