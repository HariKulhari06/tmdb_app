package com.hari.tmdb.db.internal.resultentities

import androidx.room.Embedded
import androidx.room.Relation
import com.hari.tmdb.db.internal.entity.PopularShowEntity
import com.hari.tmdb.db.internal.entity.ShowEntity

class PopularEntryWithShow : EntryWithShow<PopularShowEntity> {
    @Embedded
    override lateinit var entry: PopularShowEntity

    @Relation(parentColumn = "show_id", entityColumn = "tmdb_id")
    override var relations: List<ShowEntity> = emptyList()
}