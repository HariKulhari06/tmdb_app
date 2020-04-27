package com.hari.tmdb.db.internal.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hari.tmdb.db.internal.entity.helper.MovieEntity

@Entity(tableName = "movie")
data class MovieEntityImp(
    @ColumnInfo(name = "id") @PrimaryKey override val id: Int = 0,
    @ColumnInfo(name = "adult") override var adult: Boolean? = null,
    @ColumnInfo(name = "backdrop_path") override var backdropPath: String? = null,
    @ColumnInfo(name = "budget") override var budget: Long? = null,
    @ColumnInfo(name = "genre_ids") override var genreIds: String? = null,
    @ColumnInfo(name = "homepage") override var homepage: String? = null,
    @ColumnInfo(name = "imdb_id") override var imdbId: String? = null,
    @ColumnInfo(name = "original_language") override var originalLanguage: String? = null,
    @ColumnInfo(name = "original_title") override var originalTitle: String? = null,
    @ColumnInfo(name = "overview") override var overview: String? = null,
    @ColumnInfo(name = "popularity") override var popularity: Double? = null,
    @ColumnInfo(name = "posterPath") override var posterPath: String? = null,
    @ColumnInfo(name = "releaseDate") override var releaseDate: String? = null,
    @ColumnInfo(name = "revenue") override var revenue: Long? = null,
    @ColumnInfo(name = "runtime") override var runtime: Int? = null,
    @ColumnInfo(name = "status") override var status: String? = null,
    @ColumnInfo(name = "tag_line") override var tagLine: String? = null,
    @ColumnInfo(name = "title") override var title: String? = null,
    @ColumnInfo(name = "video") override var video: Boolean? = null,
    @ColumnInfo(name = "vote_average") override var voteAverage: Double? = null,
    @ColumnInfo(name = "vote_count") override var voteCount: Int? = null,
    @ColumnInfo(name = "production_company_path") override var productionCompanyPath: String? = null
) : MovieEntity