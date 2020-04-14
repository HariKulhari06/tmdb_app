package com.hari.tmdb.db.internal.mapper

import com.hari.tmdb.db.internal.entity.GenreEntity
import com.hari.tmdb.model.Genre

fun GenreEntity.toGenre(): Genre = Genre(id = id, name = name)