package com.hari.tmdb.repository.internal.mapper

import com.hari.tmdb.model.Genre
import com.hari.tmdb.model.mapper.Mapper
import javax.inject.Inject

class TmdbApiGenreToTmdbGenre @Inject constructor() :
    Mapper<com.uwetrottmann.tmdb2.entities.Genre, Genre> {
    override suspend fun map(from: com.uwetrottmann.tmdb2.entities.Genre) = Genre(
        id = from.id,
        name = from.name
    )
}
