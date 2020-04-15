package com.hari.tmdb.db.internal.entity

import com.hari.tmdb.model.TmdbEntity

interface MovieEntity : TmdbEntity {
    var adult: Boolean
    var backdropPath: String?
    var genreIds: String
    var originalLanguage: String
    var originalTitle: String
    var overview: String
    var popularity: Double
    var posterPath: String?
    var releaseDate: String
    var title: String
    var video: Boolean
    var voteAverage: Double
    var voteCount: Int
}

