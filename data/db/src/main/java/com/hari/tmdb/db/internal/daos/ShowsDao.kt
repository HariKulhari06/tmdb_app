package com.hari.tmdb.db.internal.daos

import androidx.room.Dao
import com.hari.tmdb.db.internal.entity.ShowEntity

@Dao
abstract class ShowsDao : EntityDao<ShowEntity>()