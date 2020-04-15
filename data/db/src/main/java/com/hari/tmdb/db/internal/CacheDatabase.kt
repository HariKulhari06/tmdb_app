package com.hari.tmdb.db.internal

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hari.tmdb.db.internal.daos.GenreDao
import com.hari.tmdb.db.internal.daos.PopularMovieDao
import com.hari.tmdb.db.internal.entity.GenreEntityImp
import com.hari.tmdb.db.internal.entity.PopularMovieEntityImp

@Database(entities = [GenreEntityImp::class, PopularMovieEntityImp::class], version = 1)
internal abstract class CacheDatabase : RoomDatabase() {
    abstract fun genreDao(): GenreDao
    abstract fun popularMovieDao(): PopularMovieDao
    fun sqlite(): SupportSQLiteDatabase {
        return mDatabase
    }
}