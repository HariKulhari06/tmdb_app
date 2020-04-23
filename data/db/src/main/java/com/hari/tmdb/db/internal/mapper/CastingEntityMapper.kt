@file:Suppress(
    "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)

package com.hari.tmdb.db.internal.mapper

import com.hari.tmdb.db.internal.entity.CastingEntityImp
import com.hari.tmdb.model.Cast
import com.hari.tmdb.model.mapper.Mapper
import com.uwetrottmann.tmdb2.entities.Movie

val tmdbMoviesToCastingImp = object : Mapper<Movie, List<CastingEntityImp>> {
    override suspend fun map(from: Movie): List<CastingEntityImp> {
        return from.credits.cast.map { castMember ->
            CastingEntityImp(
                id = castMember.id,
                movieId = from.id,
                name = castMember.name,
                castId = castMember.cast_id,
                character = castMember.character,
                creditId = castMember.credit_id,
                gender = castMember.order,
                order = castMember.order,
                profilePath = castMember.profile_path
            )
        }
    }

}


val castingEntityToCast = object : Mapper<CastingEntityImp, Cast> {
    override suspend fun map(from: CastingEntityImp): Cast {
        return Cast(
            id = from.id,
            profilePath = from.profilePath ?: "",
            order = from.order,
            gender = from.gender ?: -1,
            creditId = from.creditId,
            character = from.character,
            castId = from.castId,
            name = from.name
        )
    }

}