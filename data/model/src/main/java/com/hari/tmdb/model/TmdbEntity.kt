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

interface ShowPaginatedEntry : Entry {
    val page: Int
    val pageOrder: Int
    val showId: Int
}

interface MultipleEntry : Entry {
    val otherMovieId: Int
}
