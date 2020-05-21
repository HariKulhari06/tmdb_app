package com.hari.tmdb.imageloading.internal

import coil.map.MeasuredMapper
import coil.size.PixelSize
import coil.size.Size
import com.hari.tmdb.image.TmdbImageUrlProvider
import com.hari.tmdb.model.Image
import com.hari.tmdb.model.ImageType
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Inject

class TmdbImageEntityCoilMapper @Inject constructor(
) : MeasuredMapper<Image, HttpUrl> {

    override fun handles(data: Image): Boolean = true

    override fun map(data: Image, size: Size): HttpUrl {
        val width = if (size is PixelSize) {
            size.width
        } else 0

        val urlProvider = TmdbImageUrlProvider()

        return when (data.type) {
            ImageType.BACKDROP -> urlProvider.getBackdropUrl(data.filePath!!, width).toHttpUrl()
            ImageType.POSTER -> urlProvider.getPosterUrl(data.filePath!!, width).toHttpUrl()
            ImageType.LOGO -> urlProvider.getLogoUrl(data.filePath!!, width).toHttpUrl()
            ImageType.PROFILE -> urlProvider.getProfileUrl(data.filePath!!, width).toHttpUrl()
        }
    }
}
