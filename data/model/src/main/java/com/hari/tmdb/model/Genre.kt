package com.hari.tmdb.model

data class Genre(
    override val id: Int,
    val name: String
) : TmdbEntity