package com.hari.tmdb.repository.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hari.tmdb.model.LoadingState

private fun getErrorMessage(report: PagingRequestHelper.StatusReport): Throwable {
    return PagingRequestHelper.RequestType.values().mapNotNull {
        report.getErrorFor(it)
    }.first()
}

fun PagingRequestHelper.createStatusLiveData(): LiveData<LoadingState> {
    val liveData = MutableLiveData<LoadingState>()
    addListener { report ->
        when {
            report.hasRunning() -> {
                liveData.postValue(LoadingState.Loading)
            }
            report.hasError() -> {
                liveData.postValue(LoadingState.Error(getErrorMessage(report)))

            }
            else -> {
                liveData.postValue(LoadingState.Loaded)
            }
        }
    }
    return liveData
}
