package com.hari.tmdb.movie.viewmodel

import androidx.lifecycle.*
import com.hari.tmdb.ext.combine
import com.hari.tmdb.ext.requireValue
import com.hari.tmdb.ext.toAppError
import com.hari.tmdb.ext.toLoadingState
import com.hari.tmdb.model.*
import com.hari.tmdb.model.repository.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class DiscoverMoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {
    data class UiModel(
        val isLoading: Boolean,
        val error: AppError?,
        val filters: Filters,
        val allFilters: Filters,
        val movies: List<Movie>?
    ) {
        companion object {
            val EMPTY = UiModel(
                true,
                null,
                Filters(),
                Filters(),
                null
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
        moviesRepository.refreshFilters()
    }

    private val moviesLoadStateLiveData: LiveData<LoadState<List<Movie>>> =
        liveData(Dispatchers.IO) {
            moviesRepository.discoverMovies(filters = filterLiveData.asFlow())
                .toLoadingState()
                .collect {
                    emit(it)
                }
        }

    // Produce UiModel
    val uiModel: LiveData<UiModel> = combine(
        initialValue = UiModel.EMPTY,
        liveData1 = filtersLoadStateLiveData,
        liveData2 = filterLiveData,
        liveData3 = moviesLoadStateLiveData
    ) { current: UiModel,
        filtersLoadState: LoadState<Filters>,
        filters: Filters,
        moviesLoadState: LoadState<List<Movie>>
        ->
        val isLoading = filtersLoadState.isLoading || moviesLoadState.isLoading
        val allFilters = filtersLoadState.getValueOrNull() ?: Filters()
        UiModel(
            isLoading = isLoading,
            error = moviesLoadState.getErrorIfExists().toAppError(),
            filters = filters,
            allFilters = allFilters,
            movies = moviesLoadState.getValueOrNull()
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

    fun filterChanged(includeAdult: String, checked: Boolean) {
        val filters = filterLiveData.requireValue()
        (filters.includeAdult as MutableSet).clear()
        filterLiveData.value = filters.copy(
            includeAdult = if (checked) filters.includeAdult + includeAdult else filters.includeAdult - includeAdult
        )
    }

    fun filterChanged(sortBy: SortBy, checked: Boolean) {
        val filters = filterLiveData.requireValue()
        (filters.sortBy as MutableSet).clear()
        filterLiveData.value = filters.copy(
            sortBy = if (checked) filters.sortBy + sortBy else filters.sortBy - sortBy
        )
    }

    fun filterChanged(certificate: Certification, checked: Boolean) {
        val filters = filterLiveData.requireValue()
        (filters.certifications as MutableSet).clear()
        filterLiveData.value = filters.copy(
            certifications = if (checked) filters.certifications + certificate else filters.certifications - certificate
        )
    }

    fun filterChanged(language: Language, checked: Boolean) {
        val filters = filterLiveData.requireValue()
        (filters.languages as MutableSet).clear()
        filterLiveData.value = filters.copy(
            languages = if (checked) filters.languages + language else filters.languages - language
        )
    }
}