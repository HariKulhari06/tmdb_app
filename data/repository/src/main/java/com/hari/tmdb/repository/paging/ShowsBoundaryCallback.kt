package com.hari.tmdb.repository.paging

import androidx.paging.PagedList
import com.hari.tmdb.db.internal.ShowsDatabase
import com.hari.tmdb.ext.toResult
import com.hari.tmdb.model.ErrorResult
import com.hari.tmdb.model.Show
import com.hari.tmdb.model.Success
import com.hari.tmdb.model.mapper.toLambda
import com.hari.tmdb.repository.mapper.TvShowResultsPageToShows
import com.uwetrottmann.tmdb2.services.TvService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ShowsBoundaryCallback(
    private val showsDatabase: ShowsDatabase,
    private val tvService: TvService,
    private val scope: CoroutineScope,
    private val tvShowResultsPageMapper: TvShowResultsPageToShows,
    private val showsCategory: ShowsCategory,
    private val ioExecutor: Executor = Executors.newSingleThreadExecutor()
) : PagedList.BoundaryCallback<Show>() {

    val helper = PagingRequestHelper(ioExecutor)
    val loadingState = helper.createStatusLiveData()

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) { callback ->
            webServiceCall(callback = callback)
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: Show) {
        super.onItemAtEndLoaded(itemAtEnd)
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) { callback ->
            webServiceCall(
                page = runBlocking {
                    getPage()
                },
                callback = callback
            )
        }
    }

    private suspend fun getPage(): Int {
        return when (showsCategory) {
            ShowsCategory.POPULAR -> {
                showsDatabase.getPopularShowLastPage()
            }
        }.plus(1)
    }

    private fun webServiceCall(
        page: Int = 1,
        callback: PagingRequestHelper.Request.Callback
    ) {
        scope.launch(Dispatchers.IO) {
            try {
                val result =
                    when (showsCategory) {
                        ShowsCategory.POPULAR -> {
                            tvService.popular(page, null)
                        }
                    }
                        .execute()
                        .toResult(tvShowResultsPageMapper.toLambda())

                when (result) {
                    is Success -> {
                        showsDatabase.insertPopularShows(result.data)
                        callback.recordSuccess()
                    }
                    is ErrorResult -> {
                        callback.recordFailure(result.throwable)
                    }
                }
            } catch (e: Exception) {
                callback.recordFailure(e)
            }
        }
    }

    enum class ShowsCategory {
        POPULAR
    }
}