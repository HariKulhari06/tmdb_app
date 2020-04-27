package com.hari.tmdb.db.internal.entity.helper

import com.hari.tmdb.model.TmdbEntity

interface GenreEntity : TmdbEntity {
    override var id: Int
    var name: String?
}

