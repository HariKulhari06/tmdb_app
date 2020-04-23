package com.hari.tmdb.db.internal.resultentities

import androidx.room.Embedded
import androidx.room.Relation
import com.hari.tmdb.db.internal.entity.CastingEntityImp
import com.hari.tmdb.db.internal.entity.ImageEntityImp
import com.hari.tmdb.db.internal.entity.MovieEntityImp
import com.hari.tmdb.db.internal.entity.VideoEntity

class MovieWithContents : EntryWithMovieEntity<MovieEntityImp> {
    @Embedded
    override lateinit var entry: MovieEntityImp

    @Relation(parentColumn = "id", entityColumn = "id")
    override var relations: List<MovieEntityImp> = emptyList()

    @Relation(parentColumn = "id", entityColumn = "movie_id")
    override var images: List<ImageEntityImp> = emptyList()

    @Relation(parentColumn = "id", entityColumn = "movie_id")
    override var videos: List<VideoEntity> = emptyList()

    @Relation(parentColumn = "id", entityColumn = "movie_id")
    override var casting: List<CastingEntityImp> = emptyList()

}