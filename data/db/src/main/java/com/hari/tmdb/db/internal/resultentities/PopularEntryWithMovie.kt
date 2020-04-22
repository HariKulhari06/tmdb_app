package com.hari.tmdb.db.internal.resultentities

import androidx.room.Embedded
import androidx.room.Relation
import com.hari.tmdb.db.internal.entity.BackdropImageEntity
import com.hari.tmdb.db.internal.entity.MovieEntityImp
import com.hari.tmdb.db.internal.entity.PopularMovieEntity

class PopularEntryWithMovie : EntryWithMovie<PopularMovieEntity> {
    @Embedded
    override lateinit var entry: PopularMovieEntity

    @Relation(parentColumn = "movie_id", entityColumn = "id")
    override var relations: List<MovieEntityImp> = emptyList()

    @Relation(parentColumn = "movie_id", entityColumn = "movie_id")
    override var backdropImages: List<BackdropImageEntity> = emptyList()

}