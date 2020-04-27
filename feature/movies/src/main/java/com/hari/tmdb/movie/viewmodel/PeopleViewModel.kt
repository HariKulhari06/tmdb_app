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
import com.hari.tmdb.model.People
import com.hari.tmdb.model.repository.PeoplesRepository
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PeopleViewModel @AssistedInject constructor(
    @Assisted private val peopleId: Int,
    private val peoplesRepository: PeoplesRepository
) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            peoplesRepository.refreshPeople(peopleId)
        }
    }

    // UiModel definition
    data class UiModel(
        val isLoading: Boolean,
        val error: AppError?,
        val people: People?
    ) {
        companion object {
            val EMPTY = UiModel(
                isLoading = false,
                error = null,
                people = null
            )
        }
    }


    //LiveData
    private val peopleLoadStateLiveData: LiveData<LoadState<People>> = liveData(Dispatchers.IO) {
        peoplesRepository.peopleObserver(peopleId)
            .toLoadingState()
            .collect { emit(it) }
    }

    val ui: LiveData<
            UiModel> = combine(
        initialValue = UiModel.EMPTY,
        liveData1 = peopleLoadStateLiveData
    ) { _: UiModel,
        peopleLoadState: LoadState<People> ->

        val isLoading = peopleLoadState.isLoading

        UiModel(
            isLoading = isLoading,
            error = peopleLoadState.getErrorIfExists().toAppError(),
            people = peopleLoadState.getValueOrNull()
        )
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            peopleId: Int
        ): PeopleViewModel
    }
}

