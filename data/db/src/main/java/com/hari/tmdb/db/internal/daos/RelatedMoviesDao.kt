package com.hari.tmdb.db.internal.daos

import androidx.room.Dao
import com.hari.tmdb.db.internal.entity.RelatedMovieEntity

@Dao
abstract class RelatedMoviesDao : EntityDao<RelatedMovieEntity>() {

}
