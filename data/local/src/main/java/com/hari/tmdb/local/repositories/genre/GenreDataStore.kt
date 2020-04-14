package com.hari.tmdb.local.repositories.genre

import com.hari.tmdb.local.entities.Genre
import com.hari.tmdb.local.entities.Result

interface GenreDataStore {
    suspend fun getMovieGenres(): Result<List<Genre>>
}