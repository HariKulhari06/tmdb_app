package com.hari.tmdb.db.internal

import androidx.room.withTransaction
import com.hari.tmdb.db.internal.daos.GenreDao
import com.hari.tmdb.db.internal.entity.GenreEntity
import com.hari.tmdb.db.internal.mapper.toGenreEntity
import com.hari.tmdb.model.Genre
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class RoomDatabase @Inject constructor(
    private val cacheDatabase: CacheDatabase,
    private val genreDao: GenreDao
) : MoviesDataBase {
    override suspend fun moviesGenre(): Flow<List<GenreEntity>> {
        return genreDao.movieGenre()
    }

    override suspend fun saveMovieGenre(genres: List<Genre>) {
        cacheDatabase.withTransaction {
            genreDao.deleteAll()
            genreDao.insertAll(genres.map { it.toGenreEntity() })
        }
    }

}