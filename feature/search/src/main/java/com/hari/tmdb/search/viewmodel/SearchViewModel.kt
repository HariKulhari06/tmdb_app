package com.hari.tmdb.search.viewmodel

import androidx.lifecycle.*
import com.hari.tmdb.ext.combine
import com.hari.tmdb.ext.toAppError
import com.hari.tmdb.ext.toLoadingState
import com.hari.tmdb.model.AppError
import com.hari.tmdb.model.LoadState
import com.hari.tmdb.model.Movie
import com.hari.tmdb.model.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    val query = MutableLiveData<String>()

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
            searchRepository.search(query.asFlow())
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


    fun updateSearchQuery(s: String) {
        query.postValue(s)
    }
}