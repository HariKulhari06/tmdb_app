package com.hari.tmdb.db.internal.resultentities

import com.hari.tmdb.db.internal.entity.MovieEntityImp
import com.hari.tmdb.model.TmdbEntity
import java.util.*

interface EntryWithMovie<ET : TmdbEntity> {
    var entry: ET
    var relations: List<MovieEntityImp>

    val movieEntity: MovieEntityImp
        get() {
            assert(relations.size == 1)
            return relations[0]
        }

    fun generateStableId(): Long {
        return Objects.hash(entry::class.java.name, entry.id).toLong()
    }
}
