package com.hari.tmdb.model

data class
Movie(
    override val id: Int,
    var adult: Boolean,
    var backdropPath: String?,
    var genreIds: List<Int>,
    var originalLanguage: String,
    var originalTitle: String,
    var overview: String,
    var popularity: Double,
    var posterPath: String?,
    var releaseDate: String,
    var title: String,
    var video: Boolean,
    var voteAverage: Double,
    var voteCount: Int
) : TmdbEntity

