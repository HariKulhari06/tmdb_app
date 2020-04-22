package com.hari.tmdb.model

interface TmdbEntity {
    val id: Int
}

interface Entry : TmdbEntity {
    val movieId: Int
}

interface PaginatedEntry : Entry {
    val page: Int
}