package com.hari.tmdb.system.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hari.tmdb.ext.toNonNullSingleEvent
import com.hari.tmdb.model.AppError
import javax.inject.Inject

class SystemViewModel @Inject constructor() : ViewModel() {
    private val mutableErrorLiveData = MutableLiveData<AppError?>()
    val errorLiveData: LiveData<AppError> get() = mutableErrorLiveData.toNonNullSingleEvent()
    fun onError(error: AppError) {
        mutableErrorLiveData.value = error
    }

}