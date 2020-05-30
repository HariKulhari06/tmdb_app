package com.hari.tmdb.db.internal

import androidx.paging.DataSource
import androidx.room.withTransaction
import com.hari.tmdb.db.internal.daos.*
import com.hari.tmdb.db.internal.entity.*
import com.hari.tmdb.db.internal.mapper.*
import com.hari.tmdb.model.*
import com.hari.tmdb.model.mapper.forLists
import com.uwetrottmann.tmdb2.entities.GenreResults
import com.uwetrottmann.tmdb2.entities.MovieResultsPage
import com.uwetrottmann.tmdb2.entities.Person
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
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
    private val castingDao: CastingDao,
    private val peopleDao: PeopleDao,
    private val languageDao: LanguageDao,
    private val showsDao: ShowsDao,
    private val popularShowDao: PopularShowDao,
    private val topRatedShowDao: TopRatedShowDao,
    private val onTvShowDao: OnTvShowDao,
    private val airingTodayShowDao: AiringTodayShowDao
) : MoviesDataBase, PeoplesDatabase, ShowsDatabase {
    override suspend fun moviesGenre(): Flow<List<Genre>> {
        return genreDao.movieGenre().map { genreEntities ->
            genreEntityToGenre.forLists().invoke(genreEntities)
        }
    }

    override suspend fun languages(): Flow<List<Language>> {
        return languageDao.languages().map {
            languageEntityToLanguage.forLists().invoke(it)
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
            imageDao.saveImages(movie.id!!, tmdbMovieToImageEntityImp.map(movie))
            videoDao.saveVideos(movie.id!!, tmdbMoviesToVideoEntity.map(movie))

            movieToRelatedMovieEntity.map(movie).map {
                movieDao.insert(it.first)
                relatedMoviesDao.insert(it.second)
            }

            val castingAndPeoplesPair = tmdbMoviesToCastingImp.map(movie)

            val casting = mutableListOf<CastingEntityImp>()
            castingAndPeoplesPair.mapTo(casting) {
                it.first
            }
            castingDao.saveCasting(movie.id!!, casting)

            val peoples = mutableListOf<PeopleEntityImp>()
            castingAndPeoplesPair.mapTo(peoples) {
                it.second
            }
            insertPeoples(peoples)
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

    override suspend fun saveMovies(result: MovieResultsPage, movieCategory: MovieCategory) {
        cacheDatabase.withTransaction {
            if (result.page == 1)
                popularMovieDao.deleteAll(movieCategory)

            val movies: MutableList<MovieEntityImp> = mutableListOf<MovieEntityImp>()
            val moviesWithCategory: MutableList<PopularMovieEntity> =
                mutableListOf<PopularMovieEntity>()
            val moviesPair: List<Pair<MovieEntityImp, PopularMovieEntity>> =
                popularMovieEntryMapper.map(result)

            moviesPair.mapTo(movies) { pair ->
                pair.first
            }

            moviesPair.mapTo(moviesWithCategory) { pair ->
                pair.second.also { movie ->
                    movie.movieCategory = movieCategory
                }
            }

            movieDao.insertAll(movies)
            popularMovieDao.insertAll(moviesWithCategory)
        }
    }

    override suspend fun popularMovies(): Flow<List<Movie>> {
        return popularMovieDao.entriesObservable().map { popularEntity ->
            popularEntity.map { movieEntityToMovie.map(it.movieEntity) }
        }
    }

    override suspend fun movies(movieCategory: MovieCategory): Flow<List<Movie>> {
        return popularMovieDao.entriesObservable(movieCategory).map { popularEntity ->
            popularEntity.map { movieEntityToMovie.map(it.movieEntity) }
        }
    }

    override fun moviesDataSource(movieCategory: MovieCategory): DataSource.Factory<Int, Movie> {
        return popularMovieDao.dataSource(movieCategory).map { popularEntity ->
            runBlocking {
                movieEntityToMovie.map(popularEntity.movieEntity)
            }
        }
    }

    override suspend fun getMovieLastPage(id: Int, movieCategory: MovieCategory): Int {
        return popularMovieDao.getLastPage(id, movieCategory) ?: 1
    }

    override suspend fun insertPeoples(peoples: List<PeopleEntityImp>) {
        peopleDao.insertAll(peoples)
    }

    override suspend fun people(id: Int): Flow<People> {
        return peopleDao.people(id)
            .map { peopleEntityToPeople.map(it) }
            .distinctUntilChanged()
    }

    override suspend fun savePeople(data: Person) {
        peopleDao.insert(personToPeopleEntity.map(data))
    }

    override fun getLatestAiredShow(): Flow<Show> {
        return flow {
            airingTodayShowDao.getLatestAiredShow().collect {
                if (it != null) {
                    emit(showEntityToShow.map(it.showEntity))
                }
            }
        }
    }

    override fun getShow(id: Int): Flow<Show> {
        return showsDao.show(id).map { showEntity -> showEntityToShow.map(showEntity) }
            .distinctUntilChanged()
    }

    override suspend fun getShowId(tmdb: Int): Int {
        return showsDao.getShowId(tmdb)
    }

    override suspend fun insertShow(data: ShowEntity) {
        showsDao.insertOrUpdate(data)
    }

    override suspend fun insertPopularShows(shows: List<Pair<ShowEntity, PopularShowEntity>>) {
        cacheDatabase.withTransaction {
            shows.firstOrNull()?.let { pair ->
                if (pair.second.page == 1) {
                    popularShowDao.deleteAll()
                }
            }
            shows.map { pair ->
                showsDao.insert(pair.first)
                popularShowDao.insert(pair.second)
            }
        }
    }

    override suspend fun getPopularShowLastPage(): Int {
        return popularShowDao.getLastPage() ?: 1
    }

    override fun popularShowDataSource(): DataSource.Factory<Int, Show> {
        return popularShowDao.dataSource().map { popularShoeEntity ->
            runBlocking {
                showEntityToShow.map(popularShoeEntity.showEntity)
            }
        }
    }

    override suspend fun insertTopRatedShows(shows: List<Pair<ShowEntity, PopularShowEntity>>) {
        cacheDatabase.withTransaction {
            shows.firstOrNull()?.let { pair ->
                if (pair.second.page == 1) {
                    topRatedShowDao.deleteAll()
                }
            }
            shows.map { pair ->
                showsDao.insert(pair.first)
                topRatedShowDao.insert(
                    TopRatedShowEntity(
                        movieId = pair.second.movieId,
                        pageOrder = pair.second.pageOrder,
                        page = pair.second.page
                    )
                )
            }
        }
    }

    override suspend fun getTopRatedLastPage(): Int {
        return topRatedShowDao.getLastPage() ?: 1
    }

    override fun topRatedShowDataSource(): DataSource.Factory<Int, Show> {
        return topRatedShowDao.dataSource().map { topRatedShoeEntity ->
            runBlocking {
                showEntityToShow.map(topRatedShoeEntity.showEntity)
            }
        }
    }

    override suspend fun insertOnTvShows(shows: List<Pair<ShowEntity, PopularShowEntity>>) {
        cacheDatabase.withTransaction {
            shows.firstOrNull()?.let { pair ->
                if (pair.second.page == 1) {
                    onTvShowDao.deleteAll()
                }
            }
            shows.map { pair ->
                showsDao.insert(pair.first)
                onTvShowDao.insert(
                    OnTvShowEntity(
                        movieId = pair.second.movieId,
                        pageOrder = pair.second.pageOrder,
                        page = pair.second.page
                    )
                )
            }
        }
    }

    override suspend fun getOnTvLastPage(): Int {
        return onTvShowDao.getLastPage() ?: 1
    }

    override fun onTvShowDataSource(): DataSource.Factory<Int, Show> {
        return onTvShowDao.dataSource().map { onTvShoeEntity ->
            runBlocking {
                showEntityToShow.map(onTvShoeEntity.showEntity)
            }
        }
    }

    override suspend fun insertAiringTodayShows(shows: List<Pair<ShowEntity, PopularShowEntity>>) {
        cacheDatabase.withTransaction {
            shows.firstOrNull()?.let { pair ->
                if (pair.second.page == 1) {
                    airingTodayShowDao.deleteAll()
                }
            }
            shows.map { pair ->
                showsDao.insert(pair.first)
                airingTodayShowDao.insert(
                    AiringTodayShowEntity(
                        movieId = pair.second.movieId,
                        pageOrder = pair.second.pageOrder,
                        page = pair.second.page
                    )
                )
            }
        }
    }

    override suspend fun getAiringTodayLastPage(): Int {
        return airingTodayShowDao.getLastPage() ?: 1
    }

    override fun airingTodayShowDataSource(): DataSource.Factory<Int, Show> {
        return airingTodayShowDao.dataSource().map { onTvShoeEntity ->
            runBlocking {
                showEntityToShow.map(onTvShoeEntity.showEntity)
            }
        }
    }

}