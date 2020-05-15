package com.hari.tmdb.repository.paging

import androidx.paging.PagedList
import com.hari.tmdb.db.internal.MoviesDataBase
import com.hari.tmdb.ext.toResult
import com.hari.tmdb.model.ErrorResult
import com.hari.tmdb.model.Movie
import com.hari.tmdb.model.MovieCategory
import com.hari.tmdb.model.Success
import com.uwetrottmann.tmdb2.services.MoviesService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MovieCategoryBoundaryCallback(
    private val movieCategory: MovieCategory,
    private val moviesDataBase: MoviesDataBase,
    private val moviesService: MoviesService,
    private val scope: CoroutineScope,
    private val ioExecutor: Executor = Executors.newSingleThreadExecutor()
) : PagedList.BoundaryCallback<Movie>() {

    val helper = PagingRequestHelper(ioExecutor)
    val loadingState = helper.createStatusLiveData()

    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) { callback ->
            webServiceCall(callback = callback)
        }
    }


    override fun onItemAtEndLoaded(itemAtEnd: Movie) {
        super.onItemAtEndLoaded(itemAtEnd)
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) { callback ->
            webServiceCall(
                page = runBlocking {
                    moviesDataBase.getMovieLastPage(
                        itemAtEnd.id,
                        movieCategory
                    ).plus(1)
                },
                callback = callback
            )
        }
    }

    private fun webServiceCall(
        page: Int = 1,
        callback: PagingRequestHelper.Request.Callback
    ) {
        scope.launch(Dispatchers.IO) {
            try {
                val result = when (movieCategory) {
                    MovieCategory.POPULAR -> {
                        moviesService.popular(page, null, null)
                    }
                    MovieCategory.NOW_PLAYING -> {
                        moviesService.nowPlaying(page, null, null)
                    }
                    MovieCategory.UPCOMING -> {
                        moviesService.upcoming(page, null, null)
                    }
                    MovieCategory.TOP_RATED -> {
                        moviesService.topRated(page, null, null)
                    }
                    MovieCategory.OTHER -> {
                        moviesService.popular(1, null, null)
                    }
                }
                    .execute()
                    .toResult()

                when (result) {
                    is Success -> {
                        moviesDataBase.saveMovies(result.data, movieCategory)
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
}