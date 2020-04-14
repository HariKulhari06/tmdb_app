package com.hari.tmdb.movie.viewmodel

import androidx.lifecycle.ViewModel
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject

class MovieDetailViewModel @AssistedInject constructor(
    @Assisted private val movieId: Int
) : ViewModel() {


    @AssistedInject.Factory
    interface Factory {
        fun create(
            movieId: Int
        ): MovieDetailViewModel
    }
}