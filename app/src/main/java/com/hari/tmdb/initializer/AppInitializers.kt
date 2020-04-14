package com.hari.tmdb.initializer

import android.app.Application
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
