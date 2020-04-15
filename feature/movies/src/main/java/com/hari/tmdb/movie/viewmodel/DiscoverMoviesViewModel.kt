package com.hari.tmdb.movie.viewmodel

import androidx.lifecycle.*
import com.hari.tmdb.ext.combine
import com.hari.tmdb.ext.requireValue
import com.hari.tmdb.ext.toAppError
import com.hari.tmdb.ext.toLoadingState
import com.hari.tmdb.log.Timber
import com.hari.tmdb.model.*
import com.hari.tmdb.model.repository.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class DiscoverMoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {
    data class UiModel(
        val isLoading: Boolean,
        val error: AppError?,
        val filters: Filters,
        val allFilters: Filters
    ) {
        companion object {
            val EMPTY = UiModel(
                true,
                null,
                Filters(),
                Filters()
            )
        }
    }


    private val filterLiveData: MutableLiveData<Filters> = MutableLiveData(Filters())

    // LiveDatas
    private val filtersLoadStateLiveData: LiveData<LoadState<Filters>> = liveData {
        emitSource(
            moviesRepository.getMovieFilters()
                .toLoadingState()
                .asLiveData()
        )
        try {
            viewModelScope.launch(Dispatchers.IO) {
                moviesRepository.refresh()
                moviesRepository.refreshPopularContents()
            }
        } catch (e: Exception) {
            // We can show sessions with cache
            Timber.debug(e)
        }
    }

    // Produce UiModel
    val uiModel: LiveData<UiModel> = combine(
        initialValue = UiModel.EMPTY,
        liveData1 = filtersLoadStateLiveData,
        liveData2 = filterLiveData
    ) { current: UiModel,
        filtersLoadState: LoadState<Filters>,
        filters: Filters
        ->
        val isLoading = filtersLoadState.isLoading
        val allFilters = filtersLoadState.getValueOrNull() ?: Filters()
        UiModel(
            isLoading = isLoading,
            error = filtersLoadState.getErrorIfExists().toAppError(),
            filters = filters,
            allFilters = allFilters
        )
    }


    fun resetFilter() {
        filterLiveData.value = Filters()
    }

    fun filterChanged(genre: Genre, checked: Boolean) {
        val filters = filterLiveData.requireValue()
        filterLiveData.value = filters.copy(
            genres = if (checked) filters.genres + genre else filters.genres - genre
        )
    }

    fun filterChanged(includeAdult: Boolean, checked: Boolean) {
        val filters = filterLiveData.requireValue()
        filterLiveData.value = filters.copy(
            includeAdult = if (checked) filters.includeAdult + includeAdult else filters.includeAdult - includeAdult
        )
    }

    fun filterChanged(sortBy: SortBy, checked: Boolean) {
        val filters = filterLiveData.requireValue()
        filterLiveData.value = filters.copy(
            sortBy = if (checked) filters.sortBy + sortBy else filters.sortBy - sortBy
        )
    }
}