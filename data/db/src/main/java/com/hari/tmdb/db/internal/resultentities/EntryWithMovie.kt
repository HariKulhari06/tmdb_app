package com.hari.tmdb.db.internal.resultentities

import com.hari.tmdb.db.internal.entity.BackdropImageEntity
import com.hari.tmdb.db.internal.entity.MovieEntityImp
import com.hari.tmdb.model.Entry
import java.util.*

interface EntryWithMovie<ET : Entry> {
    var entry: ET
    var relations: List<MovieEntityImp>
    var backdropImages: List<BackdropImageEntity>

    val movieEntity: MovieEntityImp
        get() {
            assert(relations.size == 1)
            return relations[0]
        }

    fun generateStableId(): Long {
        return Objects.hash(entry::class.java.name, entry.movieId).toLong()
    }
}
