package com.hari.tmdb.db.internal

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hari.tmdb.db.internal.daos.BackdropImageDao
import com.hari.tmdb.db.internal.daos.GenreDao
import com.hari.tmdb.db.internal.daos.MovieDao
import com.hari.tmdb.db.internal.daos.PopularMovieDao
import com.hari.tmdb.db.internal.entity.BackdropImageEntity
import com.hari.tmdb.db.internal.entity.GenreEntityImp
import com.hari.tmdb.db.internal.entity.MovieEntityImp
import com.hari.tmdb.db.internal.entity.PopularMovieEntity

@Database(
    entities = [
        GenreEntityImp::class,
        MovieEntityImp::class,
        PopularMovieEntity::class,
        BackdropImageEntity::class
    ],
    version = 1,
    exportSchema = false
)
internal abstract class CacheDatabase : RoomDatabase() {
    abstract fun genreDao(): GenreDao
    abstract fun movieDao(): MovieDao
    abstract fun popularMovieDao(): PopularMovieDao
    abstract fun backdropImageFao(): BackdropImageDao
    fun sqlite(): SupportSQLiteDatabase {
        return mDatabase
    }
}