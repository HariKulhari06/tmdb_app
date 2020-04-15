package com.hari.tmdb.db.internal.mapper

import com.hari.tmdb.db.internal.entity.PopularMovieEntityImp
import com.hari.tmdb.model.Movie

fun Movie.toMovieEntity(): PopularMovieEntityImp =
    PopularMovieEntityImp(
        id = id,
        adult = adult,
        backdropPath = backdropPath,
        genreIds = genreIds.joinToString(separator = ","),
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount
    )
