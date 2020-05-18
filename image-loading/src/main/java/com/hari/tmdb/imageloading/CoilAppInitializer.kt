package com.hari.tmdb.imageloading

import android.app.Application
import coil.Coil
import coil.ImageLoader
import com.hari.tmdb.appinitializer.AppInitializer
import javax.inject.Inject

class CoilAppInitializer @Inject constructor() : AppInitializer {
    override fun initialize(application: Application) {
        Coil.setDefaultImageLoader {
            ImageLoader(application) {
                // Hardware bitmaps break with our transitions, disable them for now
                allowHardware(false)
                // Since we don't use hardware bitmaps, we can pool bitmaps and use a higher
                // ratio of memory
                bitmapPoolPercentage(0.5)
/*
                componentRegistry {
                    add(tmdbImageEntityMapper)
                    add(episodeEntityMapper)
                }*/
            }
        }
    }
}