package com.hari.tmdb.settings

interface TmdbPreferences {

    fun setup()

    var themePreference: Theme
    var useLessData: Boolean

    enum class Theme {
        LIGHT,
        DARK,
        BATTERY_SAVER_ONLY,
        SYSTEM
    }
}
