package com.hari.tmdb.shows.internal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.hari.tmdb.model.Listing
import com.hari.tmdb.model.Show
import com.hari.tmdb.model.repository.ShowsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShowsViewModel @Inject constructor(
    private val showsRepository: ShowsRepository
) : ViewModel() {

    private val popularShowListing: Listing<Show> =
        showsRepository.popularShowsListing(viewModelScope)

    val popularShoePagedList: LiveData<PagedList<Show>> = popularShowListing.pagedList


    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            showsRepository.refreshPopularShows()
        }
    }


}