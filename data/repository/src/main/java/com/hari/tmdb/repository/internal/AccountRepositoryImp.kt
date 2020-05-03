package com.hari.tmdb.repository.internal

import com.hari.tmdb.ext.bodyOrThrow
import com.hari.tmdb.model.GuestSession
import com.hari.tmdb.model.RequestToken
import com.hari.tmdb.model.Session
import com.hari.tmdb.model.repository.AccountRepository
import com.uwetrottmann.tmdb2.services.AuthenticationService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AccountRepositoryImp @Inject constructor(
    private val authenticationService: AuthenticationService
) : AccountRepository {
    override suspend fun login(username: String, password: String): Flow<Session> {
        return requestToken()
            .transform { requestToken ->
                emitAll(validateToken(username, password, requestToken.requestToken))
            }
            .transform { requestToken: RequestToken ->
                emitAll(createSession(requestToken.requestToken))
            }
    }

    override suspend fun requestToken(): Flow<RequestToken> = flow {
        val result = authenticationService.requestToken()
            .execute()
            .bodyOrThrow()
        emit(RequestToken(result.success, result.request_token, result.expires_at))
    }

    override suspend fun validateToken(
        username: String,
        password: String,
        requestToken: String
    ): Flow<RequestToken> = flow {
        val result = authenticationService.validateToken(username, password, requestToken)
            .execute()
            .bodyOrThrow()
        emit(RequestToken(result.success, result.request_token, result.expires_at))
    }

    override suspend fun createSession(requestToken: String): Flow<Session> = flow {
        val result = authenticationService.createSession(requestToken)
            .execute()
            .bodyOrThrow()
        emit(Session(result.success, result.session_id))
    }

    override suspend fun createGuestSession(): Flow<GuestSession> = flow {
        val result = authenticationService.createGuestSession()
            .execute()
            .bodyOrThrow()
        emit(GuestSession(result.success, result.guest_session_id, result.expires_at))
    }
}