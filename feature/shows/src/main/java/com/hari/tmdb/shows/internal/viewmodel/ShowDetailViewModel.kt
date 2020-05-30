package com.hari.tmdb.shows.internal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hari.tmdb.model.repository.ShowsRepository
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ShowDetailViewModel @AssistedInject constructor(
    @Assisted private val showId: Int,
    private val showsRepository: ShowsRepository
) : ViewModel() {
    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            showsRepository.refreshShow(showId).collect { }
        }
    }

    val show = showsRepository.getShow(showId).asLiveData()

    @AssistedInject.Factory
    interface Factory {
        fun create(
            showId: Int
        ): ShowDetailViewModel
    }
}