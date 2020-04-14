package com.hari.tmdb.ui

import androidx.annotation.IdRes
import com.hari.tmdb.R

enum class PageConfiguration(
    val id: Int,
    val isIndigoBackground: Boolean = false,
    val hasTitle: Boolean = true,
    val isShowLogoImage: Boolean = false,
    val hideToolbar: Boolean = false,
    val hideBottomNavigationMenu: Boolean = false
) {

    SPLASH(
        R.id.splash,
        isIndigoBackground = false,
        hasTitle = false,
        hideToolbar = true,
        hideBottomNavigationMenu = true
    ),
    LOGIN(R.id.login, hasTitle = false, hideToolbar = true, hideBottomNavigationMenu = true),
    MAIN(
        R.id.main,
        isIndigoBackground = true,
        hasTitle = true,
        isShowLogoImage = true
    ),
    DETAIL(
        R.id.movieDetails,
        hasTitle = true,
        hideBottomNavigationMenu = true,
        isIndigoBackground = false
    ),
    ACCOUNT(
        R.id.account,
        hasTitle = true,
        hideToolbar = true
    ),
    OTHER(0);

    operator fun component1() = id
    operator fun component2() = isIndigoBackground
    operator fun component3() = hasTitle
    operator fun component4() = isShowLogoImage
    operator fun component5() = hideToolbar
    operator fun component6() = hideBottomNavigationMenu

    companion object {
        fun getConfiguration(@IdRes id: Int): PageConfiguration {
            return PageConfiguration
                .values()
                .firstOrNull { it.id == id } ?: OTHER
        }
    }
}
