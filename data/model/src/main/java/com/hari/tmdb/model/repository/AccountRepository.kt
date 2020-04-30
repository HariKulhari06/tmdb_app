package com.hari.tmdb.model.repository

import com.hari.tmdb.model.GuestSession
import com.hari.tmdb.model.RequestToken
import com.hari.tmdb.model.Session
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    suspend fun login(username: String, password: String): Flow<Session>
    suspend fun requestToken(): Flow<RequestToken>
    suspend fun validateToken(
        username: String,
        password: String,
        requestToken: String
    ): Flow<RequestToken>

    suspend fun createSession(requestToken: String): Flow<Session>
    suspend fun createGuestSession(): Flow<GuestSession>
}