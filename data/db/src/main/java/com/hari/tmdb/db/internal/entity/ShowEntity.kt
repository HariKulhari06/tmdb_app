package com.hari.tmdb.db.internal.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.hari.tmdb.model.ShowStatus
import com.hari.tmdb.model.TmdbEntity

@Entity(tableName = "shows", indices = [Index("tmdb_id", unique = true)])
data class ShowEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") override var id: Int = 0,
    @ColumnInfo(name = "title") val title: String? = null,
    @ColumnInfo(name = "original_title") val originalTitle: String? = null,
    @ColumnInfo(name = "tmdb_id") val tmdbId: Int? = null,
    @ColumnInfo(name = "overview") val summary: String? = null,
    @ColumnInfo(name = "homepage") val homepage: String? = null,
    @ColumnInfo(name = "popularity") val popularity: Double? = null,
    @ColumnInfo(name = "vote_average") val voteAverage: Double? = null,
    @ColumnInfo(name = "vote_count") val voteCount: Int? = null,
    @ColumnInfo(name = "certification") val certification: String? = null,
    @ColumnInfo(name = "poster_path") val posterPath: String? = null,
    @ColumnInfo(name = "backdrop_path") val backdrop_path: String? = null,
    @ColumnInfo(name = "first_aired") val firstAired: String? = null,
    @ColumnInfo(name = "last_aired") val lastAired: String? = null,
    @ColumnInfo(name = "next_aired") val nextAir: String? = null,
    @ColumnInfo(name = "country") val country: String? = null,
    @ColumnInfo(name = "original_language") val originalLanguage: String? = null,
    @ColumnInfo(name = "languages") val languages: String? = null,
    @ColumnInfo(name = "network") val network: String? = null,
    @ColumnInfo(name = "network_logo_path") val networkLogoPath: String? = null,
    @ColumnInfo(name = "runtime") val runtime: Int? = null,
    @ColumnInfo(name = "genres") val _genres: String? = null,
    @ColumnInfo(name = "status") val status: ShowStatus = ShowStatus.ENDED,
    @ColumnInfo(name = "number_of_seasons") val numberOfSeasons: Int? = null,
    @ColumnInfo(name = "number_of_episodes") val numberOfEpisodes: Int? = null,
    @ColumnInfo(name = "created_by") val createdBy: String? = null
) : TmdbEntity