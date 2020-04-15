package com.hari.tmdb.db.internal.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "popular_movie")
data class PopularMovieEntityImp(
    @PrimaryKey override val id: Int,
    override var adult: Boolean,
    override var backdropPath: String?,
    override var genreIds: String,
    override var originalLanguage: String,
    override var originalTitle: String,
    override var overview: String,
    override var popularity: Double,
    override var posterPath: String?,
    override var releaseDate: String,
    override var title: String,
    override var video: Boolean,
    override var voteAverage: Double,
    override var voteCount: Int
) : MovieEntity {
    fun genreId(): List<Int> = genreIds.split(",").map { it.toInt() }
}