@file:Suppress(
    "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)

package com.hari.tmdb.db.internal.mapper

import com.hari.tmdb.db.internal.entity.GenreEntityImp
import com.hari.tmdb.db.internal.entity.helper.GenreEntity
import com.hari.tmdb.model.Genre
import com.hari.tmdb.model.mapper.Mapper
import com.uwetrottmann.tmdb2.entities.GenreResults
import com.uwetrottmann.tmdb2.entities.Genre as TmdbGenre

val genreResultsToGenre = object : Mapper<GenreResults, List<GenreEntityImp>> {
    override suspend fun map(from: GenreResults): List<GenreEntityImp> {
        return from.genres.map { genreToGenreEntity.map(it) }
    }
}

val genreToGenreEntity = object : Mapper<TmdbGenre, GenreEntityImp> {
    override suspend fun map(from: TmdbGenre): GenreEntityImp =
        GenreEntityImp(
            id = from.id,
            name = from.name
        )
}

val genreEntityToGenre = object : Mapper<GenreEntity, Genre> {
    override suspend fun map(from: GenreEntity): Genre = Genre(
        id = from.id,
        name = from.name ?: ""
    )
}