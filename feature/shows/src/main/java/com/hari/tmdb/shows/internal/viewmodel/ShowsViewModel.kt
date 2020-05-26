package com.hari.tmdb.shows.internal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.hari.tmdb.model.Listing
import com.hari.tmdb.model.Show
import com.hari.tmdb.model.repository.ShowsRepository
import javax.inject.Inject

class ShowsViewModel @Inject constructor(
    private val showsRepository: ShowsRepository
) : ViewModel() {

    private val popularShowListing: Listing<Show> =
        showsRepository.popularShowsListing(viewModelScope)
    private val topRatedShowListing: Listing<Show> =
        showsRepository.topRatedShowsListing(viewModelScope)
    private val onTvShowListing: Listing<Show> =
        showsRepository.onTvShowsListing(viewModelScope)
    private val airingTodayShowListing: Listing<Show> =
        showsRepository.airingTodayShowsListing(viewModelScope)

    val latestAiredShow = showsRepository.getLatestAiredShow().asLiveData()

    val popularShoePagedList: LiveData<PagedList<Show>> = popularShowListing.pagedList
    val topRatedShoePagedList: LiveData<PagedList<Show>> = topRatedShowListing.pagedList
    val onTvShoePagedList: LiveData<PagedList<Show>> = onTvShowListing.pagedList
    val airingTodayShoePagedList: LiveData<PagedList<Show>> = airingTodayShowListing.pagedList


    fun refresh() {
        popularShowListing.refresh.invoke()
        topRatedShowListing.refresh.invoke()
        onTvShowListing.refresh.invoke()
        airingTodayShowListing.refresh.invoke()
    }


}