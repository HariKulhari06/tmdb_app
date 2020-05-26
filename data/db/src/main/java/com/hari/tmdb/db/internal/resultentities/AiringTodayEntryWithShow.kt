package com.hari.tmdb.db.internal.resultentities

import androidx.room.Embedded
import androidx.room.Relation
import com.hari.tmdb.db.internal.entity.AiringTodayShowEntity
import com.hari.tmdb.db.internal.entity.ShowEntity

class AiringTodayEntryWithShow : EntryWithShow<AiringTodayShowEntity> {
    @Embedded
    override lateinit var entry: AiringTodayShowEntity

    @Relation(parentColumn = "show_id", entityColumn = "tmdb_id")
    override var relations: List<ShowEntity> = emptyList()
}