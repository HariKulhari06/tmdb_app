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
import com.hari.tmdb.model.MoviePage
import com.hari.tmdb.model.repository.MoviesRepository
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MoviePageViewModel @AssistedInject constructor(
    @Assisted private val moviePage: MoviePage,
    private val moviesRepository: MoviesRepository
) : ViewModel() {


    // UiModel definition
    data class UiModel(
        val isLoading: Boolean,
        val error: AppError?,
        val movies: List<Movie>?
    ) {
        companion object {
            val EMPTY = UiModel(
                isLoading = false,
                error = null,
                movies = null
            )
        }
    }

    //LiveData
    private val moviesLoadStateLiveData: LiveData<LoadState<List<Movie>>> = liveData {
        if (moviePage == MoviePage.POPULAR)
            moviesRepository.popularContents(1)
                .toLoadingState()
                .collect { emit(it) }

        refresh()
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            moviesRepository.refreshPopularContents()
        }
    }

    val ui: LiveData<UiModel> = combine(
        initialValue = UiModel.EMPTY,
        liveData1 = moviesLoadStateLiveData
    ) { _: UiModel,
        moviesLoadState: LoadState<List<Movie>> ->

        val isLoading = moviesLoadState.isLoading

        UiModel(
            isLoading = isLoading,
            error = moviesLoadState.getErrorIfExists().toAppError(),
            movies = moviesLoadState.getValueOrNull()
        )
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            moviePage: MoviePage
        ): MoviePageViewModel
    }

}
