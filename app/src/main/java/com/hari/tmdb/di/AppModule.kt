package com.hari.tmdb.di

import android.app.Application
import android.content.Context
import com.hari.tmdb.BuildConfig
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [AppModuleBinds::class])
class AppModule {
    @Provides
    fun provideAppContext(application: Application): Context {
        return application
    }

    @Provides
    @Named("tmdb-api")
    fun provideTmdbApiKey(): String = BuildConfig.TMDB_API_KEY
}
