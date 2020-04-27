package com.hari.tmdb.db.internal.entity.helper

import com.hari.tmdb.model.TmdbEntity

interface PeopleEntity : TmdbEntity {
    val adult: Boolean?
    val alsoKnownAs: String?
    val biography: String?
    val birthday: String?
    val deathday: String?
    val gender: Int?
    val homepage: String?
    val imdbId: String?
    val knownForDepartment: String?
    val name: String?
    val placeOfBirth: String?
    val popularity: Double?
    val profilePath: String?
}