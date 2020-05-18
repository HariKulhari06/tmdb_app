package com.hari.tmdb.initializer

import android.app.Application
import com.hari.tmdb.appinitializer.AppInitializer
import javax.inject.Inject

class AppInitializers @Inject constructor(
    private val initializers: Set<@JvmSuppressWildcards AppInitializer>
) {
    fun initialize(application: Application) {
        initializers.forEach {
            it.initialize(application)
        }
    }
}
