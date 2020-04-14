package com.hari.tmdb.db.internal

import android.content.Context
import androidx.room.Room
import com.hari.tmdb.db.internal.daos.GenreDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [DbModule.Providers::class])
internal abstract class DbModule {
    @Binds
    abstract fun moviesDataBase(impl: RoomDatabase): MoviesDataBase

    @Module
    internal object Providers {
        @Singleton
        @Provides
        fun cacheDatabase(
            context: Context
        ): CacheDatabase {
            return Room.databaseBuilder(
                context,
                CacheDatabase::class.java,
                "mooov.db"
            )
                .build()
        }

        @Provides
        fun genreDao(database: CacheDatabase): GenreDao {
            return database.genreDao()
        }
    }
}