package com.hari.tmdb.db.internal

import androidx.room.withTransaction
import com.hari.tmdb.db.internal.daos.GenreDao
import com.hari.tmdb.db.internal.daos.MovieDao
import com.hari.tmdb.db.internal.daos.PopularMovieDao
import com.hari.tmdb.db.internal.mapper.*
import com.hari.tmdb.model.Genre
import com.hari.tmdb.model.Movie
import com.hari.tmdb.model.mapper.forLists
import com.uwetrottmann.tmdb2.entities.GenreResults
import com.uwetrottmann.tmdb2.entities.MovieResultsPage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.uwetrottmann.tmdb2.entities.Movie as TmdbMovie

internal class RoomDatabase @Inject constructor(
    private val cacheDatabase: CacheDatabase,
    private val genreDao: GenreDao,
    private val movieDao: MovieDao,
    private val popularMovieDao: PopularMovieDao
) : MoviesDataBase {
    override suspend fun moviesGenre(): Flow<List<Genre>> {
        return genreDao.movieGenre().map { genreEntities ->
            genreEntityToGenre.forLists().invoke(genreEntities)
        }
    }

    override suspend fun saveMovieGenre(result: GenreResults) {
        genreDao.insertAll(genreResultsToGenre.map(result))
    }

    override suspend fun movie(id: Int): Flow<Movie> =
        movieDao.movie(id).map { movieEntity ->
            val movie = movieEntityToMovie.map(movieEntity)
            movie.genres = genreEntityToGenre.forLists()
                .invoke(genreDao.movieGenre(movieEntity.genreIds?.split(",")?.map { it.toInt() }
                    ?: emptyList()))
            movie
        }

    override suspend fun saveMovie(movie: TmdbMovie) {
        cacheDatabase.withTransaction {
            movieDao.update(movieToMovieEntityMapper.map(movie))
        }
    }

    override suspend fun savePopularMovies(result: MovieResultsPage) {
        cacheDatabase.withTransaction {
            if (result.page == 1)
                popularMovieDao.deleteAll()
            popularMovieEntryMapper.map(result).map {
                movieDao.insert(it.first)
                popularMovieDao.insert(it.second)
            }
        }
    }

    override suspend fun popularMovies(): Flow<List<Movie>> {
        return popularMovieDao.entriesObservable().map { popularEntity ->
            popularEntity.map { movieEntityToMovie.map(it.movieEntity) }
        }
    }

}