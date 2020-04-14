package com.hari.tmdb.local.mappers

import com.hari.tmdb.local.entities.Genre
import com.hari.tmdb.model.mapper.Mapper
import com.uwetrottmann.tmdb2.entities.GenreResults
import javax.inject.Inject

class TmdbApiGenreResultsToTmdbGenre @Inject constructor(
    private val tmdbGenreMapper: TmdbApiGenreToTmdbGenre
) : Mapper<GenreResults, List<Genre>> {

    override suspend fun map(from: GenreResults): List<Genre> {
        val list = mutableListOf<Genre>()
        from.genres?.forEach {
            list.add(tmdbGenreMapper.map(it))
        }
        return list
    }
}