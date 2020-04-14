package com.hari.tmdb.local.repositories.genre

import com.hari.tmdb.local.entities.Genre
import com.hari.tmdb.local.entities.Success
import javax.inject.Inject

class GenreRepository @Inject constructor(
    private val tmdbDataSource: TmdbGenreDataSource
) {
    suspend fun getMovieGenres(): List<Genre> {
        return when (val tmdbResult = tmdbDataSource.getMovieGenres()) {
            is Success -> tmdbResult.data
            else -> emptyList()
        }
    }
}