package com.hari.tmdb.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hari.tmdb.ext.combine
import com.hari.tmdb.ext.toAppError
import com.hari.tmdb.ext.toLoadingState
import com.hari.tmdb.model.AppError
import com.hari.tmdb.model.LoadState
import com.hari.tmdb.model.Movie
import com.hari.tmdb.model.repository.SearchRepository
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf

class KeywordSearchResultViewModel @AssistedInject constructor(
    @Assisted private val query: String,
    private val searchRepository: SearchRepository
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
    private val moviesLoadStateLiveData: LiveData<LoadState<List<Movie>>> =
        liveData(Dispatchers.IO) {
            searchRepository.search(flowOf(query))
                .toLoadingState()
                .collect { emit(it) }
        }


    val ui: LiveData<UiModel> = combine(
        initialValue = UiModel.EMPTY,
        liveData1 = moviesLoadStateLiveData
    ) { _: UiModel,
        movieLoadState: LoadState<List<Movie>> ->

        val isLoading = movieLoadState.isLoading

        UiModel(
            isLoading = isLoading,
            error = movieLoadState.getErrorIfExists().toAppError(),
            movies = movieLoadState.getValueOrNull()
        )

    }


    @AssistedInject.Factory
    interface Factory {
        fun create(
            query: String
        ): KeywordSearchResultViewModel
    }
}