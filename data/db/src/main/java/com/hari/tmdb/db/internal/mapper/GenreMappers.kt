package com.hari.tmdb.db.internal.mapper

import com.hari.tmdb.db.internal.entity.GenreEntityImp
import com.hari.tmdb.model.Genre

fun Genre.toGenreEntity(): GenreEntityImp =
    GenreEntityImp(
        id = id,
        name = name
    )

