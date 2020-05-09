package com.hari.tmdb.settings.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hari.tmdb.ext.combine
import com.hari.tmdb.model.NightMode
import javax.inject.Inject

class SettingsViewModel @Inject constructor() : ViewModel() {
    data class UiModel(
        val nightMode: NightMode
    ) {
        companion object {
            val EMPTY = UiModel(NightMode.SYSTEM)
        }
    }

    private val nightModeLiveData: MutableLiveData<NightMode> = MutableLiveData(NightMode.SYSTEM)

    var uiModel: LiveData<UiModel> = combine(
        initialValue = UiModel.EMPTY,
        liveData1 = nightModeLiveData
    ) { uiModel: UiModel, nightMode: NightMode ->
        UiModel(
            nightMode = nightMode
        )
    }

    fun setNightMode(newValue: NightMode) {
        nightModeLiveData.value = newValue
    }
}