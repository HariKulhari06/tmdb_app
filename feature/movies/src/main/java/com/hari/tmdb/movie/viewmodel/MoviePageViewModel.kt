package com.hari.tmdb.movie.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hari.tmdb.model.Listing
import com.hari.tmdb.model.Movie
import com.hari.tmdb.model.MovieCategory
import com.hari.tmdb.model.repository.MoviesRepository
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject

class MoviePageViewModel @AssistedInject constructor(
    @Assisted private val moviesCategory: MovieCategory,
    private val moviesRepository: MoviesRepository
) : ViewModel() {
    private val moviesResult: Listing<Movie> =
        moviesRepository.moviesPagedList(moviesCategory, viewModelScope)

    val movies = moviesResult.pagedList
    val loadingState = moviesResult.loadingState
    val refreshState = moviesResult.refreshState

    fun refresh() {
        moviesResult.refresh.invoke()
    }

    fun retry() {
        moviesResult.retry.invoke()
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            moviesCategory: MovieCategory
        ): MoviePageViewModel
    }

}
