package com.hari.tmdb.db.internal

import com.hari.tmdb.db.internal.daos.GenreDao
import com.hari.tmdb.db.internal.daos.PopularMovieDao
import com.hari.tmdb.db.internal.entity.GenreEntity
import com.hari.tmdb.db.internal.mapper.toGenreEntity
import com.hari.tmdb.db.internal.mapper.toMovieEntity
import com.hari.tmdb.model.Genre
import com.hari.tmdb.model.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class RoomDatabase @Inject constructor(
    private val cacheDatabase: CacheDatabase,
    private val genreDao: GenreDao,
    private val popularMovieDao: PopularMovieDao
) : MoviesDataBase {
    override suspend fun moviesGenre(): Flow<List<GenreEntity>> {
        return genreDao.movieGenre()
    }

    override suspend fun saveMovieGenre(genres: List<Genre>) {
        genreDao.insertAll(genres.map { it.toGenreEntity() })
    }

    override suspend fun savePopularMovies(movies: List<Movie>) {
        popularMovieDao.insertAll(movies.map { it.toMovieEntity() })
    }

}