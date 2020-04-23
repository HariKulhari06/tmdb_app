package com.hari.tmdb.model

data class Cast(
    val castId: Int,
    val character: String,
    val creditId: String,
    val gender: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val profilePath: String
)