package com.hari.tmdb.model

data class Image(
    var filePath: String?,
    var width: Int,
    var height: Int,
    var iso6391: String? = null,
    var aspectRatio: Double,
    var voteAverage: Double,
    var voteCount: Int
)