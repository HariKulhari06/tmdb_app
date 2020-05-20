package com.hari.tmdb.model

data class Image(
    var filePath: String?,
    var width: Int = 0,
    var height: Int = 0,
    var iso6391: String? = null,
    var aspectRatio: Double = 0.0,
    var voteAverage: Double = 0.0,
    var voteCount: Int = 0,
    var type: ImageType
)