package com.hari.tmdb.model

data class People(
    val adult: Boolean?,
    val alsoKnownAs: List<String> = emptyList(),
    val biography: String?,
    val birthday: String?,
    val deathday: Any?,
    val gender: Int?,
    val homepage: String?,
    val id: Int?,
    val imdbId: String?,
    val knownForDepartment: String?,
    val name: String?,
    val placeOfBirth: String?,
    val popularity: Double?,
    val profilePath: String?
)