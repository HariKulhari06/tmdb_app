package com.hari.tmdb.db.internal.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hari.tmdb.db.internal.entity.helper.PeopleEntity

@Entity(tableName = "peoples")
data class PeopleEntityImp(
    @PrimaryKey @ColumnInfo(name = "id") override val id: Int = 0,
    @ColumnInfo(name = "adult") override val adult: Boolean? = null,
    @ColumnInfo(name = "also_known_as") override val alsoKnownAs: String? = null,
    @ColumnInfo(name = "biography") override val biography: String? = null,
    @ColumnInfo(name = "birthday") override val birthday: String? = null,
    @ColumnInfo(name = "deathday") override val deathday: String? = null,
    @ColumnInfo(name = "gender") override val gender: Int? = null,
    @ColumnInfo(name = "homepage") override val homepage: String? = null,
    @ColumnInfo(name = "imdbId") override val imdbId: String? = null,
    @ColumnInfo(name = "known_for_department") override val knownForDepartment: String? = null,
    @ColumnInfo(name = "name") override val name: String? = null,
    @ColumnInfo(name = "place_of_birth") override val placeOfBirth: String? = null,
    @ColumnInfo(name = "popularity") override val popularity: Double? = null,
    @ColumnInfo(name = "profile_path") override val profilePath: String? = null
) : PeopleEntity