package com.hari.tmdb.model

data class
Movie(
    override val id: Int,
    var adult: Boolean,
    var backdropPath: String?,
    var budget: Long = 0,
    var genres: List<Genre>? = null,
    var homepage: String? = null,
    var imdbId: String? = null,
    var originalLanguage: String,
    var originalTitle: String,
    var overview: String,
    var popularity: Double,
    var posterPath: String?,
    var releaseDate: String,
    var revenue: Long = 0,
    var runtime: Int = 0,
    var status: String? = null,
    var tagLine: String? = null,
    var title: String,
    var video: Boolean,
    var voteAverage: Double,
    var voteCount: Int,
    var productionCompanyPath: String? = null
) : TmdbEntity

