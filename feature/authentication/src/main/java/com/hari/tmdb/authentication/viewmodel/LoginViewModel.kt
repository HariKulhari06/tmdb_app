package com.hari.tmdb.authentication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hari.tmdb.ext.toLoadingState
import com.hari.tmdb.model.repository.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    fun login(userName: String, password: String) = liveData(Dispatchers.IO) {
        accountRepository.login(userName, password)
            .toLoadingState()
            .collect {
                emit(it)
            }
    }


    fun loginAsGuest() = liveData(Dispatchers.IO) {
        accountRepository.createGuestSession()
            .toLoadingState()
            .collect {
                emit(it)
            }
    }

}