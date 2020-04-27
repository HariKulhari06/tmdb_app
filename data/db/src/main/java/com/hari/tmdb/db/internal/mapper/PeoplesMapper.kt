@file:Suppress(
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS"
)

package com.hari.tmdb.db.internal.mapper

import com.hari.tmdb.db.internal.entity.PeopleEntityImp
import com.hari.tmdb.model.People
import com.hari.tmdb.model.mapper.Mapper
import com.uwetrottmann.tmdb2.entities.Person

val peopleEntityToPeople = object : Mapper<PeopleEntityImp, People> {
    override suspend fun map(from: PeopleEntityImp): People {
        return People(
            id = from.id,
            name = from.name,
            adult = from.adult,
            alsoKnownAs = from.alsoKnownAs?.split(",") ?: emptyList(),
            biography = from.biography,
            birthday = from.birthday,
            deathday = from.deathday,
            gender = from.gender,
            homepage = from.homepage,
            imdbId = from.imdbId,
            knownForDepartment = from.knownForDepartment,
            placeOfBirth = from.placeOfBirth,
            popularity = from.popularity,
            profilePath = from.profilePath
        )
    }

}


val personToPeopleEntity = object : Mapper<Person, PeopleEntityImp> {
    override suspend fun map(from: Person): PeopleEntityImp {
        return PeopleEntityImp(
            id = from.id!!,
            profilePath = from.profile_path,
            name = from.name,
            popularity = from.popularity,
            placeOfBirth = from.place_of_birth,
            imdbId = from.imdb_id,
            homepage = from.homepage,
            gender = from.gender,
            deathday = from.deathday?.toString(),
            alsoKnownAs = from.also_known_as?.joinToString { it.toString() },
            birthday = from.birthday.toString(),
            biography = from.biography,
            adult = from.adult
        )
    }

}