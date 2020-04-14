package com.hari.tmdb.di

import android.app.Application
import com.hari.tmdb.db.DbComponent
import com.hari.tmdb.db.internal.MoviesDataBase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
object DbComponentModule {

    @Provides
    @Singleton
    fun provideMoviesDatabase(
        application: Application
    ): MoviesDataBase {
        return DbComponent.factory()
            .create(application, Dispatchers.IO)
            .moviesDataBase()
    }
}