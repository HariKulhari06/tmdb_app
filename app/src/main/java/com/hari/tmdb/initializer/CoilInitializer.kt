package com.hari.tmdb.initializer

import android.app.Application
import com.hari.tmdb.appinitializer.AppInitializer
import com.hari.tmdb.image.CoilInitializer
import javax.inject.Inject

class CoilInitializer @Inject constructor() :
    AppInitializer {
    override fun initialize(application: Application) {
        CoilInitializer.init(application)
    }
}
