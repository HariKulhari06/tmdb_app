package com.hari.tmdb.db.internal.entity.helper

import com.hari.tmdb.model.TmdbEntity

interface CastingHelper : TmdbEntity {
    val castId: Int
    val character: String?
    val creditId: String
    val gender: Int?
    val name: String
    val order: Int
    val profilePath: String?
}