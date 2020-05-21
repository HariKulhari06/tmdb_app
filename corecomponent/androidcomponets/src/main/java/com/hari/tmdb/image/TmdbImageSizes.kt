package com.hari.tmdb.image

/**
 * ## Add Supported Image Sizes
Min Res      Max Res
poster   = Poster ............  500 x 750   2000 x 3000
backdrop = Fanart ............ 1280 x 720   3840 x 2160
still    = TV Show Episode ... 1280 x 720   3840 x 2160
profile  = Actors Actresses ..  300 x 450   2000 x 3000
logo     = TMDb Logo

## API Supported Image Sizes

|  poster  | backdrop |  still   | profile  |   logo   |
| :------: | :------: | :------: | :------: | :------: |
| -------- | -------- | -------- |    w45   |    w45   |
|    w92   | -------- |    w92   | -------- |    w92   |
|   w154   | -------- | -------- | -------- |   w154   |
|   w185   | -------- |   w185   |   w185   |   w185   |
| -------- |   w300   |   w300   | -------- |   w300   |
|   w342   | -------- | -------- | -------- | -------- |
|   w500   | -------- | -------- | -------- |   w500   |
| -------- | -------- | -------- |   h632   | -------- |
|   w780   |   w780   | -------- | -------- | -------- |
| -------- |  w1280   | -------- | -------- | -------- |
| original | original | original | original | original |

Original Size is the size of the uploaded image.
It can be between Minimum Resolution and Maximum Resolution.
 */
object TmdbImageSizes {

    const val baseImageUrl = "https://image.tmdb.org/t/p/"

    val posterSizes = listOf(
        "w92",
        "w154",
        "w185",
        "w342",
        "w500",
        "w780",
        "original"
    )

    val backdropSizes = listOf(
        "w300",
        "w780",
        "w1280",
        "original"
    )

    val logoSizes = listOf(
        "w45",
        "w92",
        "w154",
        "w185",
        "w300",
        "w500",
        "original"
    )

    val profileSizes = listOf(
        "w45",
        "w185",
        "h632",
        "original"
    )

    val stillSizes = listOf(
        "w92",
        "w185",
        "w300",
        "original"
    )

}
