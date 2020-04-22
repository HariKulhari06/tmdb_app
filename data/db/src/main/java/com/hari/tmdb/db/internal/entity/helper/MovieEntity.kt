package com.hari.tmdb.db.internal.entity.helper

import com.hari.tmdb.model.TmdbEntity

interface MovieEntity : TmdbEntity {
    var adult: Boolean?
    var backdropPath: String?
    var budget: Long?
    var genreIds: String?
    var homepage: String?
    var imdbId: String?
    var originalLanguage: String?
    var originalTitle: String?
    var overview: String?
    var popularity: Double?
    var posterPath: String?
    var releaseDate: String?
    var revenue: Long?
    var runtime: Int?
    var status: String?
    var tagLine: String?
    var title: String?
    var video: Boolean?
    var voteAverage: Double?
    var voteCount: Int?
    var productionCompanyPath: String?
}
