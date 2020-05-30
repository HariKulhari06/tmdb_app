package com.hari.tmdb.repository.internal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.paging.toLiveData
import com.hari.tmdb.db.internal.ShowsDatabase
import com.hari.tmdb.db.internal.entity.PopularShowEntity
import com.hari.tmdb.db.internal.entity.ShowEntity
import com.hari.tmdb.ext.executeWithRetry
import com.hari.tmdb.ext.toResult
import com.hari.tmdb.log.Timber
import com.hari.tmdb.model.*
import com.hari.tmdb.model.mapper.toLambda
import com.hari.tmdb.model.repository.ShowsRepository
import com.hari.tmdb.repository.mapper.TvShowResultsPageToShows
import com.hari.tmdb.repository.mapper.TvShowToShowEntity
import com.hari.tmdb.repository.paging.ShowsBoundaryCallback
import com.uwetrottmann.tmdb2.entities.AppendToResponse
import com.uwetrottmann.tmdb2.enumerations.AppendToResponseItem
import com.uwetrottmann.tmdb2.services.TvService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class ShowsRepositoryImp @Inject constructor(
    private val tvShowResultsPageMapper: TvShowResultsPageToShows,
    private val tvShowMapper: TvShowToShowEntity,
    private val showsDatabase: ShowsDatabase,
    private val tvService: TvService
) : ShowsRepository {
    override fun getLatestAiredShow(): Flow<Show> {
        return showsDatabase.getLatestAiredShow()
    }

    override fun getShow(id: Int): Flow<Show> {
        return showsDatabase.getShow(id)
    }

    override fun refreshShow(id: Int): Flow<LoadingState> {
        return flow {
            emit(LoadingState.Loading)

            try {
                when (val result: Result<ShowEntity> =
                    tvService.tv(
                        id,
                        null,
                        AppendToResponse(
                            AppendToResponseItem.SIMILAR,
                            AppendToResponseItem.CREDITS,
                            AppendToResponseItem.VIDEOS
                        )
                    )
                        .executeWithRetry()
                        .toResult(tvShowMapper.toLambda())) {
                    is Success -> {
                        val show = result.data
                        show.id = showsDatabase.getShowId(id)
                        showsDatabase.insertShow(show)
                        emit(LoadingState.Loaded)
                    }
                    is ErrorResult -> {
                        Timber.error(result.throwable)
                        emit(LoadingState.Error(result.throwable))
                    }
                }
            } catch (e: Exception) {
                emit(LoadingState.Error(e))
            }
        }
    }

    override suspend fun refreshPopularShows(): LiveData<LoadingState> {
        val loadingState = MutableLiveData<LoadingState>()
        loadingState.postValue(LoadingState.Loading)
        when (val result: Result<List<Pair<ShowEntity, PopularShowEntity>>> =
            tvService.popular(1, null)
                .executeWithRetry()
                .toResult(tvShowResultsPageMapper.toLambda())) {
            is Success -> {
                showsDatabase.insertPopularShows(result.data)
                loadingState.postValue(LoadingState.Loaded)
            }
            is ErrorResult -> {
                Timber.error(result.throwable)
                loadingState.postValue(LoadingState.Error(result.throwable))
            }
        }
        return loadingState
    }

    override fun popularShowsListing(scope: CoroutineScope): Listing<Show> {

        // create a boundary callback which will observe when the user reaches to the edges of
        // the list and update the database with extra data.
        val boundaryCallback = ShowsBoundaryCallback(
            scope = scope,
            tvService = tvService,
            showsDatabase = showsDatabase,
            tvShowResultsPageMapper = tvShowResultsPageMapper,
            showsCategory = ShowsBoundaryCallback.ShowsCategory.POPULAR
        )

        // we are using a mutable live data to trigger refresh requests which eventually calls
        // refresh method and gets a new live data. Each refresh request by the user becomes a newly
        // dispatched data in refreshTrigger
        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState: LiveData<LoadingState> = refreshTrigger.switchMap {
            liveData(Dispatchers.IO) {
                emitSource(refreshPopularShows())
            }
        }

        // We use toLiveData Kotlin extension function here, you could also use LivePagedListBuilder
        val pagedList = showsDatabase
            .popularShowDataSource()
            .toLiveData(
                pageSize = 20,
                boundaryCallback = boundaryCallback
            )

        return Listing(
            pagedList = pagedList,
            loadingState = boundaryCallback.loadingState,
            retry = {
                boundaryCallback.helper.retryAllFailed()
            },
            refresh = {
                refreshTrigger.value = null
            },
            refreshState = refreshState
        )
    }

    override suspend fun refreshTopRatedShows(): LiveData<LoadingState> {
        val loadingState = MutableLiveData<LoadingState>()
        loadingState.postValue(LoadingState.Loading)
        when (val result: Result<List<Pair<ShowEntity, PopularShowEntity>>> =
            tvService.topRated(1, null)
                .executeWithRetry()
                .toResult(tvShowResultsPageMapper.toLambda())) {
            is Success -> {
                showsDatabase.insertTopRatedShows(result.data)
                loadingState.postValue(LoadingState.Loaded)
            }
            is ErrorResult -> {
                Timber.error(result.throwable)
                loadingState.postValue(LoadingState.Error(result.throwable))
            }
        }
        return loadingState
    }

    override fun topRatedShowsListing(scope: CoroutineScope): Listing<Show> {

        // create a boundary callback which will observe when the user reaches to the edges of
        // the list and update the database with extra data.
        val boundaryCallback = ShowsBoundaryCallback(
            scope = scope,
            tvService = tvService,
            showsDatabase = showsDatabase,
            tvShowResultsPageMapper = tvShowResultsPageMapper,
            showsCategory = ShowsBoundaryCallback.ShowsCategory.TOP_RATED
        )

        // we are using a mutable live data to trigger refresh requests which eventually calls
        // refresh method and gets a new live data. Each refresh request by the user becomes a newly
        // dispatched data in refreshTrigger
        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState: LiveData<LoadingState> = refreshTrigger.switchMap {
            liveData(Dispatchers.IO) {
                emitSource(refreshTopRatedShows())
            }
        }

        // We use toLiveData Kotlin extension function here, you could also use LivePagedListBuilder
        val pagedList = showsDatabase
            .topRatedShowDataSource()
            .toLiveData(
                pageSize = 20,
                boundaryCallback = boundaryCallback
            )

        return Listing(
            pagedList = pagedList,
            loadingState = boundaryCallback.loadingState,
            retry = {
                boundaryCallback.helper.retryAllFailed()
            },
            refresh = {
                refreshTrigger.value = null
            },
            refreshState = refreshState
        )
    }

    override suspend fun refreshOnTvShows(): LiveData<LoadingState> {
        val loadingState = MutableLiveData<LoadingState>()
        loadingState.postValue(LoadingState.Loading)
        when (val result: Result<List<Pair<ShowEntity, PopularShowEntity>>> =
            tvService.onTheAir(1, null)
                .executeWithRetry()
                .toResult(tvShowResultsPageMapper.toLambda())) {
            is Success -> {
                showsDatabase.insertOnTvShows(result.data)
                loadingState.postValue(LoadingState.Loaded)
            }
            is ErrorResult -> {
                Timber.error(result.throwable)
                loadingState.postValue(LoadingState.Error(result.throwable))
            }
        }
        return loadingState
    }

    override fun onTvShowsListing(scope: CoroutineScope): Listing<Show> {

        // create a boundary callback which will observe when the user reaches to the edges of
        // the list and update the database with extra data.
        val boundaryCallback = ShowsBoundaryCallback(
            scope = scope,
            tvService = tvService,
            showsDatabase = showsDatabase,
            tvShowResultsPageMapper = tvShowResultsPageMapper,
            showsCategory = ShowsBoundaryCallback.ShowsCategory.ON_TV
        )

        // we are using a mutable live data to trigger refresh requests which eventually calls
        // refresh method and gets a new live data. Each refresh request by the user becomes a newly
        // dispatched data in refreshTrigger
        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState: LiveData<LoadingState> = refreshTrigger.switchMap {
            liveData(Dispatchers.IO) {
                emitSource(refreshOnTvShows())
            }
        }

        // We use toLiveData Kotlin extension function here, you could also use LivePagedListBuilder
        val pagedList = showsDatabase
            .onTvShowDataSource()
            .toLiveData(
                pageSize = 20,
                boundaryCallback = boundaryCallback
            )

        return Listing(
            pagedList = pagedList,
            loadingState = boundaryCallback.loadingState,
            retry = {
                boundaryCallback.helper.retryAllFailed()
            },
            refresh = {
                refreshTrigger.value = null
            },
            refreshState = refreshState
        )
    }

    override suspend fun refreshAiringTodayShows(): LiveData<LoadingState> {
        val loadingState = MutableLiveData<LoadingState>()
        loadingState.postValue(LoadingState.Loading)
        when (val result: Result<List<Pair<ShowEntity, PopularShowEntity>>> =
            tvService.airingToday(1, null)
                .executeWithRetry()
                .toResult(tvShowResultsPageMapper.toLambda())) {
            is Success -> {
                showsDatabase.insertAiringTodayShows(result.data)
                loadingState.postValue(LoadingState.Loaded)
            }
            is ErrorResult -> {
                Timber.error(result.throwable)
                loadingState.postValue(LoadingState.Error(result.throwable))
            }
        }
        return loadingState
    }

    override fun airingTodayShowsListing(scope: CoroutineScope): Listing<Show> {

        // create a boundary callback which will observe when the user reaches to the edges of
        // the list and update the database with extra data.
        val boundaryCallback = ShowsBoundaryCallback(
            scope = scope,
            tvService = tvService,
            showsDatabase = showsDatabase,
            tvShowResultsPageMapper = tvShowResultsPageMapper,
            showsCategory = ShowsBoundaryCallback.ShowsCategory.AIRING_TODAY
        )

        // we are using a mutable live data to trigger refresh requests which eventually calls
        // refresh method and gets a new live data. Each refresh request by the user becomes a newly
        // dispatched data in refreshTrigger
        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState: LiveData<LoadingState> = refreshTrigger.switchMap {
            liveData(Dispatchers.IO) {
                emitSource(refreshAiringTodayShows())
            }
        }

        // We use toLiveData Kotlin extension function here, you could also use LivePagedListBuilder
        val pagedList = showsDatabase
            .airingTodayShowDataSource()
            .toLiveData(
                pageSize = 20,
                boundaryCallback = boundaryCallback
            )

        return Listing(
            pagedList = pagedList,
            loadingState = boundaryCallback.loadingState,
            retry = {
                boundaryCallback.helper.retryAllFailed()
            },
            refresh = {
                refreshTrigger.value = null
            },
            refreshState = refreshState
        )
    }

}