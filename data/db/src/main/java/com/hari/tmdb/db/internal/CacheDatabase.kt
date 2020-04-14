package com.hari.tmdb.db.internal

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hari.tmdb.db.internal.daos.GenreDao
import com.hari.tmdb.db.internal.entity.GenreEntityImp

@Database(entities = [GenreEntityImp::class], version = 1)
internal abstract class CacheDatabase : RoomDatabase() {
    abstract fun genreDao(): GenreDao
    fun sqlite():SupportSQLiteDatabase{
        return mDatabase
    }
}