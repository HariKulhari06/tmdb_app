package com.hari.tmdb.imageloading

import coil.map.MeasuredMapper
import coil.size.PixelSize
import coil.size.Size
import com.hari.tmdb.image.TmdbImageUrlProvider
import com.hari.tmdb.model.Movie
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Inject

class MovieCoilMapper @Inject constructor(
) : MeasuredMapper<Movie, HttpUrl> {

    override fun handles(data: Movie): Boolean = data.posterPath != null

    override fun map(data: Movie, size: Size): HttpUrl {
        val width = if (size is PixelSize) size.width else 0
        val urlProvider = TmdbImageUrlProvider()
        return urlProvider.getPosterUrl(data.posterPath!!, width).toHttpUrl()
    }
}
