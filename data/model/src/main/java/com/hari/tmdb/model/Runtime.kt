package com.hari.tmdb.model

data class Runtime(
    var gte: Int? = null,
    var lte: Int? = null
) {
    companion object {
        val EMPTY = Runtime(-1, -1)
    }

}