package com.hari.tmdb.ui

import androidx.annotation.IdRes
import com.hari.tmdb.R

enum class PageConfiguration(
    val id: Int,
    val isIndigoBackground: Boolean = false,
    val hasTitle: Boolean = true,
    val isShowLogoImage: Boolean = false,
    val hideToolbar: Boolean = false,
    val hideBottomNavigationMenu: Boolean = false,
    val isTopLevel: Boolean = false
) {

    SPLASH(
        R.id.splash,
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
        hideBottomNavigationMenu = true
    ),
    ACCOUNT(
        R.id.account,
        hasTitle = true
    ),
    CASTING(
        R.id.people,
        hasTitle = true,
        hideBottomNavigationMenu = true
    ),
    SEARCH(
        R.id.search,
        hasTitle = false,
        hideBottomNavigationMenu = true
    ),
    KEYWORD_SEARCH(
        R.id.keywordSearchResult,
        hasTitle = true,
        hideBottomNavigationMenu = true
    ),
    TV_SHOWS(
        R.id.shows,
        hasTitle = true
    ),
    TV_SHOWS_DETAIL(
        R.id.shows_detail,
        hasTitle = false,
        hideBottomNavigationMenu = true
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
            return values()
                .firstOrNull { it.id == id } ?: OTHER
        }
    }
}
