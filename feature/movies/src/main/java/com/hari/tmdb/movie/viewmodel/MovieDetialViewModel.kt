package com.hari.tmdb.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.hari.tmdb.ext.combine
import com.hari.tmdb.ext.toAppError
import com.hari.tmdb.ext.toLoadingState
import com.hari.tmdb.model.AppError
import com.hari.tmdb.model.LoadState
import com.hari.tmdb.model.Movie
import com.hari.tmdb.model.repository.MoviesRepository
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MovieDetailViewModel @AssistedInject constructor(
    @Assisted private val movieId: Int,
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            moviesRepository.refreshMovieDetails(movieId)
        }
    }

    // UiModel definition
    data class UiModel(
        val isLoading: Boolean,
        val error: AppError?,
        val movie: Movie?
    ) {
        companion object {
            val EMPTY = UiModel(
                isLoading = false,
                error = null,
                movie = null
            )
        }
    }

    //LiveData
    private val movieLoadStateLiveData: LiveData<LoadState<Movie>> = liveData(Dispatchers.IO) {
        moviesRepository.movieContents(movieId)
            .toLoadingState()
            .collect { emit(it) }
    }

    val ui: LiveData<UiModel> = combine(
        initialValue = UiModel.EMPTY,
        liveData1 = movieLoadStateLiveData
    ) { _: UiModel,
        movieLoadState: LoadState<Movie> ->

        val isLoading = movieLoadState.isLoading

        UiModel(
            isLoading = isLoading,
            error = movieLoadState.getErrorIfExists().toAppError(),
            movie = movieLoadState.getValueOrNull()
        )
    }


    @AssistedInject.Factory
    interface Factory {
        fun create(
            movieId: Int
        ): MovieDetailViewModel
    }
}