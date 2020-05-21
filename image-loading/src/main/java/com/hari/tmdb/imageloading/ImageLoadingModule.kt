package com.hari.tmdb.imageloading

import com.hari.tmdb.appinitializer.AppInitializer
import com.hari.tmdb.imageloading.internal.CoilAppInitializer
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

@Module
abstract class ImageLoadingModule {
    @Binds
    @IntoSet
    abstract fun provideCoilInitializer(bind: CoilAppInitializer): AppInitializer
}