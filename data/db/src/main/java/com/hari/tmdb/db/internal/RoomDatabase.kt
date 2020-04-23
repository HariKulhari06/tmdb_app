package com.hari.tmdb.db.internal

import androidx.room.withTransaction
import com.hari.tmdb.db.internal.daos.*
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

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
internal class RoomDatabase @Inject constructor(
    private val cacheDatabase: CacheDatabase,
    private val genreDao: GenreDao,
    private val movieDao: MovieDao,
    private val popularMovieDao: PopularMovieDao,
    private val imageDao: ImageDao,
    private val relatedMoviesDao: RelatedMoviesDao,
    private val videoDao: VideoDao,
    private val castingDao: CastingDao
) : MoviesDataBase {
    override suspend fun moviesGenre(): Flow<List<Genre>> {
        return genreDao.movieGenre().map { genreEntities ->
            genreEntityToGenre.forLists().invoke(genreEntities)
        }
    }

    override suspend fun saveMovieGenre(result: GenreResults) {
        genreDao.insertAll(genreResultsToGenre.map(result))
    }

    override suspend fun movie(id: Int): Flow<Movie> {
        return movieDao.entriesObservable(id).map { movieContents ->

            val movie = movieEntityToMovie.map(movieContents.entry)

            movie.genres = genreEntityToGenre.forLists()
                .invoke(
                    genreDao.movieGenre(
                        movieContents.entry.genreIds?.split(",")?.map { it.toInt() }
                            ?: emptyList()))

            movie.videos =
                movieContents.videos.map { videoEntity -> videoEntityToVideo.map(videoEntity) }

            movie.cast =
                movieContents.casting.map { castingEntityImp ->
                    castingEntityToCast.map(
                        castingEntityImp
                    )
                }

            movie
        }
    }

    override suspend fun saveMovie(movie: TmdbMovie) {
        cacheDatabase.withTransaction {
            movieDao.update(movieToMovieEntityMapper.map(movie))
            imageDao.saveImages(movie.id, tmdbMovieToImageEntityImp.map(movie))
            videoDao.saveVideos(movie.id, tmdbMoviesToVideoEntity.map(movie))
            castingDao.saveCasting(movie.id, tmdbMoviesToCastingImp.map(movie))
            movieToRelatedMovieEntity.map(movie).map {
                movieDao.insert(it.first)
                relatedMoviesDao.insert(it.second)
            }
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