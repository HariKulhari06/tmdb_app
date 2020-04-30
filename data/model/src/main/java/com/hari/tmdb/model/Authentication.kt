package com.hari.tmdb.model

data class RequestToken(
    val success: Boolean,
    val requestToken: String,
    val expiresAt: String
)


data class Session(
    val success: Boolean,
    val sessionId: String
)


data class GuestSession(
    val success: Boolean,
    val guest_session_id: String,
    val expires_at: String
)


