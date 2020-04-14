package com.hari.tmdb.local.entities

data class Genre(
    override val id: Int,
    val name: String
) : TmdbEntity