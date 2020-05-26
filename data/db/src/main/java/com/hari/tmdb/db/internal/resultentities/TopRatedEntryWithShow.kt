package com.hari.tmdb.db.internal.resultentities

import androidx.room.Embedded
import androidx.room.Relation
import com.hari.tmdb.db.internal.entity.ShowEntity
import com.hari.tmdb.db.internal.entity.TopRatedShowEntity

class TopRatedEntryWithShow : EntryWithShow<TopRatedShowEntity> {
    @Embedded
    override lateinit var entry: TopRatedShowEntity

    @Relation(parentColumn = "show_id", entityColumn = "tmdb_id")
    override var relations: List<ShowEntity> = emptyList()
}