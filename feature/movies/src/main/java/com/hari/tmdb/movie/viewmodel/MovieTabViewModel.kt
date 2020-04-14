package com.hari.tmdb.movie.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.hari.tmdb.ext.requireValue
import com.hari.tmdb.model.ExpandFilterState
import javax.inject.Inject

class MovieTabViewModel @Inject constructor() : ViewModel() {
    data class UiModel(val expandFilterState: ExpandFilterState)

    private val mutableExpandFilter = MutableLiveData(ExpandFilterState.EXPANDED)

    val uiModel = mutableExpandFilter.map {
        UiModel(it)
    }

    fun toggleExpand() {
        mutableExpandFilter.value = mutableExpandFilter.requireValue().toggledState()
    }

    fun setExpand(state: ExpandFilterState) {
        mutableExpandFilter.value = state
    }
}
