package com.hari.tmdb.initializer

import android.app.Application

interface AppInitializer {
    fun initialize(application: Application)
}
