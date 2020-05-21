package com.hari.tmdb.imageloading.internal

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.api.load
import coil.api.loadAny
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import coil.transform.Transformation
import com.hari.tmdb.model.Image
import com.hari.tmdb.model.ImageType


@BindingAdapter(
    "youtubeThumbnail"
)
fun ImageView.loadYoutubeThumbnail(
    key: String?
) {
    load("https://img.youtube.com/vi/${key}/hqdefault.jpg")
}

@BindingAdapter(
    "tmdbProfilePath",
    "circularCrop",
    requireAll = false
)
fun ImageView.loadCircularProfile(
    path: String?,
    isCircularCrop: Boolean?
) {
    loadAny(Image(filePath = path, type = ImageType.PROFILE)) {
        isCircularCrop?.let {
            if (it)
                transformations(CircleCropTransformation())
        }
    }
}


@BindingAdapter(
    "tmdbPosterPath",
    "imageSaturateOnLoad",
    requireAll = false
)
fun ImageView.loadPoster(
    oldPath: String?,
    oldSaturateOnLoad: Boolean?,
    path: String?,
    saturateOnLoad: Boolean?
) {
    if (oldPath != path || oldSaturateOnLoad != saturateOnLoad) {
        loadImage(
            null,
            oldSaturateOnLoad,
            0f,
            path?.let { Image(filePath = path, type = ImageType.POSTER) },
            saturateOnLoad,
            0f
        )
    }
}

@BindingAdapter(
    "tmdbBackdropPath",
    "imageSaturateOnLoad",
    requireAll = false
)
fun ImageView.loadBackdrop(
    oldPath: String?,
    oldSaturateOnLoad: Boolean?,
    path: String?,
    saturateOnLoad: Boolean?
) {
    if (oldPath != path || oldSaturateOnLoad != saturateOnLoad) {
        loadImage(
            null,
            oldSaturateOnLoad,
            0f,
            path?.let { Image(filePath = path, type = ImageType.BACKDROP) },
            saturateOnLoad,
            0f
        )
    }
}

@BindingAdapter(
    "tmdbLogoPath",
    "imageSaturateOnLoad",
    requireAll = false
)
fun ImageView.loadLogo(
    oldPath: String?,
    oldSaturateOnLoad: Boolean?,
    path: String?,
    saturateOnLoad: Boolean?
) {
    if (oldPath != path || oldSaturateOnLoad != saturateOnLoad) {
        loadImage(
            null,
            oldSaturateOnLoad,
            0f,
            path?.let { Image(filePath = path, type = ImageType.LOGO) },
            saturateOnLoad,
            0f
        )
    }
}

@BindingAdapter(
    "image",
    "imageSaturateOnLoad",
    "imageCornerRadius",
    requireAll = false
)
fun ImageView.loadImage(
    oldImage: Image?,
    oldSaturateOnLoad: Boolean?,
    oldCornerRadius: Float,
    image: Image?,
    saturateOnLoad: Boolean?,
    cornerRadius: Float
) {
    if (oldImage == image &&
        oldSaturateOnLoad == saturateOnLoad &&
        oldCornerRadius == cornerRadius
    ) return

    loadAny(image) {

        val transformations = ArrayList<Transformation>()
        if (cornerRadius > 0) {
            transformations += RoundedCornersTransformation(cornerRadius)
        }
        if (image?.type == ImageType.LOGO) {
            transformations += TrimTransparentEdgesTransformation
        }
        transformations(transformations)
    }
}
