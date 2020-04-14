package com.hari.tmdb.movie.viewmodel

import androidx.lifecycle.ViewModel
import com.hari.tmdb.model.repository.MoviesRepository
import javax.inject.Inject

class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel(){

}