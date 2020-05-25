package com.hari.tmdb.model

data class Show(
    val tmdbId: Int,
    val title: String? = null,
    val originalTitle: String? = null,
    val summary: String? = null,
    val homepage: String? = null,
    val popularity: Double? = null,
    val voteAverage: Double? = null,
    val voteCount: Int? = null,
    val certification: String? = null,
    val firstAired: String? = null,
    val lastAired: String? = null,
    val nextAir: String? = null,
    val country: String? = null,
    val originalLanguage: String? = null,
    val network: String? = null,
    val networkLogoPath: String? = null,
    val runtime: Int? = null,
    val status: ShowStatus,
    val numberOfSeasons: Int? = null,
    val numberOfEpisodes: Int? = null
)