package com.hari.tmdb.local.repositories.genre

import com.hari.tmdb.ext.executeWithRetry
import com.hari.tmdb.ext.toResult
import com.hari.tmdb.local.entities.ErrorResult
import com.hari.tmdb.local.entities.Genre
import com.hari.tmdb.local.entities.Result
import com.hari.tmdb.local.mappers.TmdbApiGenreResultsToTmdbGenre
import com.hari.tmdb.model.mapper.toLambda
import com.uwetrottmann.tmdb2.Tmdb
import javax.inject.Inject

class TmdbGenreDataSource @Inject constructor(
    private val tmdb: Tmdb,
    private val mapper: TmdbApiGenreResultsToTmdbGenre
) : GenreDataStore {
    override suspend fun getMovieGenres(): Result<List<Genre>> = try {
        tmdb.genreService().movie(null)
            .executeWithRetry()
            .toResult(mapper.toLambda())
    } catch (t: Throwable) {
        ErrorResult(t)
    }
}