package com.hari.tmdb.initializer

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.hari.tmdb.appinitializer.AppInitializer
import com.hari.tmdb.util.Prefs
import javax.inject.Inject

class ThemeInitializer @Inject constructor() :
    AppInitializer {
    override fun initialize(application: Application) {
        AppCompatDelegate.setDefaultNightMode(Prefs(application).getNightMode())
    }
}
