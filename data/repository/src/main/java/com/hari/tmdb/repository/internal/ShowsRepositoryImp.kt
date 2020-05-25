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
import com.hari.tmdb.repository.paging.ShowsBoundaryCallback
import com.uwetrottmann.tmdb2.services.TvService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


class ShowsRepositoryImp @Inject constructor(
    private val tvShowResultsPageMapper: TvShowResultsPageToShows,
    private val showsDatabase: ShowsDatabase,
    private val tvService: TvService
) : ShowsRepository {

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

}