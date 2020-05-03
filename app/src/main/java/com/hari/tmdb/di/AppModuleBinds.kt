package com.hari.tmdb.di

import com.hari.tmdb.initializer.AppInitializer
import com.hari.tmdb.initializer.AppInjector
import com.hari.tmdb.initializer.CoilInitializer
import com.hari.tmdb.initializer.ThemeInitializer
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

@Module
abstract class AppModuleBinds {
    @Binds
    @IntoSet
    abstract fun provideAppInjector(bind: AppInjector): AppInitializer

    @Binds
    @IntoSet
    abstract fun provideCoilInitializer(bind: CoilInitializer): AppInitializer

    @Binds
    @IntoSet
    abstract fun provideThemeInitializer(bind: ThemeInitializer): AppInitializer


}