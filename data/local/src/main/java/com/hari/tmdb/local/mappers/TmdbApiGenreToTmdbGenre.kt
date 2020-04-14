package com.hari.tmdb.local.mappers

import com.hari.tmdb.local.entities.Genre
import com.hari.tmdb.model.mapper.Mapper

class TmdbApiGenreToTmdbGenre : Mapper<com.uwetrottmann.tmdb2.entities.Genre, Genre> {
    override suspend fun map(from: com.uwetrottmann.tmdb2.entities.Genre) = Genre(
        id = from.id,
        name = from.name
    )
}
