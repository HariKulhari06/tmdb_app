package com.hari.tmdb.util

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.hari.tmdb.androidcomponents.R

class Prefs(
    private val context: Context
) {

    fun getNightMode(): Int {
        return when (PreferenceManager.getDefaultSharedPreferences(context).getString(
            DARK_THEME_KEY,
            context.getString(R.string.pref_theme_default)
        )) {
            context.getString(R.string.pref_theme_value_dark) -> {
                AppCompatDelegate.MODE_NIGHT_YES
            }
            context.getString(R.string.pref_theme_value_light) -> {
                AppCompatDelegate.MODE_NIGHT_NO
            }
            context.getString(R.string.pref_theme_value_battery) -> {
                AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
            }
            else -> {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        }
    }

    companion object {
        private const val DARK_THEME_KEY = "darkTheme"
    }
}
