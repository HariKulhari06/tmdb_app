package com.hari.tmdb.model

import androidx.annotation.DrawableRes

data class AccountsItem(
    @DrawableRes val icon: Int,
    val title: String,
    val showArrow: Boolean = true
)