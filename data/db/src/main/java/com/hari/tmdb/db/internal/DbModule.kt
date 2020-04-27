package com.hari.tmdb.db.internal

import android.content.Context
import androidx.room.Room
import com.hari.tmdb.db.internal.daos.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [DbModule.Providers::class])
internal abstract class DbModule {
    @Binds
    abstract fun moviesDataBase(impl: RoomDatabase): MoviesDataBase

    @Binds
    abstract fun peoplesDataBase(impl: RoomDatabase): PeoplesDatabase

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

        @Provides
        fun movieDao(database: CacheDatabase): MovieDao {
            return database.movieDao()
        }

        @Provides
        fun popularMovieDao(database: CacheDatabase): PopularMovieDao {
            return database.popularMovieDao()
        }

        @Provides
        fun imageDao(database: CacheDatabase): ImageDao {
            return database.imageDao()
        }

        @Provides
        fun relatedMovieDao(database: CacheDatabase): RelatedMoviesDao {
            return database.relatedMovieDao()
        }

        @Provides
        fun videoDao(database: CacheDatabase): VideoDao {
            return database.videoDao()
        }

        @Provides
        fun castingDao(database: CacheDatabase): CastingDao {
            return database.castingDao()
        }

        @Provides
        fun peopleDao(database: CacheDatabase): PeopleDao {
            return database.peopleDao()
        }
    }
}