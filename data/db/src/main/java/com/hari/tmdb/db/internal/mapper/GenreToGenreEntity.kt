package com.hari.tmdb.db.internal.mapper

import com.hari.tmdb.db.internal.entity.GenreEntityImp
import com.hari.tmdb.model.Genre
import com.hari.tmdb.model.mapper.Mapper
import javax.inject.Inject

class GenreToGenreEntity @Inject constructor() : Mapper<Genre, GenreEntityImp> {
    override suspend fun map(from: Genre): GenreEntityImp {
        return GenreEntityImp(
            id = from.id,
            name = from.name
        )
    }
}

fun Genre.toGenreEntity(): GenreEntityImp =
    GenreEntityImp(
        id = id,
        name = name
    )

