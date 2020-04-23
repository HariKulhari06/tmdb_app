package com.hari.tmdb.db.internal.entity.helper

import com.hari.tmdb.model.ImageType
import com.hari.tmdb.model.TmdbEntity

interface ImageEntity : TmdbEntity {
    var filePath: String?
    var movieId: Int
    var width: Int?
    var height: Int?
    var iso6391: String?
    var aspectRatio: Double?
    var voteAverage: Double?
    var voteCount: Int?
    var type: ImageType
}

