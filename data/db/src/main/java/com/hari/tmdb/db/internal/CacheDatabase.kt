package com.hari.tmdb.db.internal

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hari.tmdb.db.internal.daos.*
import com.hari.tmdb.db.internal.entity.*

@Database(
    entities = [
        GenreEntityImp::class,
        MovieEntityImp::class,
        PopularMovieEntity::class,
        ImageEntityImp::class,
        RelatedMovieEntity::class,
        VideoEntity::class,
        CastingEntityImp::class,
        PeopleEntityImp::class
    ],
    version = 1
)
@TypeConverters(AppTypeConverters::class)
internal abstract class CacheDatabase : RoomDatabase() {
    abstract fun genreDao(): GenreDao
    abstract fun movieDao(): MovieDao
    abstract fun popularMovieDao(): PopularMovieDao
    abstract fun imageDao(): ImageDao
    abstract fun relatedMovieDao(): RelatedMoviesDao
    abstract fun videoDao(): VideoDao
    abstract fun castingDao(): CastingDao
    abstract fun peopleDao(): PeopleDao
    fun sqlite(): SupportSQLiteDatabase {
        return mDatabase
    }
}