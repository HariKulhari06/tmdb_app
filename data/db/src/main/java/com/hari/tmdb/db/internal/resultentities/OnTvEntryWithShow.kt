package com.hari.tmdb.db.internal.resultentities

import androidx.room.Embedded
import androidx.room.Relation
import com.hari.tmdb.db.internal.entity.OnTvShowEntity
import com.hari.tmdb.db.internal.entity.ShowEntity

class OnTvEntryWithShow : EntryWithShow<OnTvShowEntity> {
    @Embedded
    override lateinit var entry: OnTvShowEntity

    @Relation(parentColumn = "show_id", entityColumn = "tmdb_id")
    override var relations: List<ShowEntity> = emptyList()
}