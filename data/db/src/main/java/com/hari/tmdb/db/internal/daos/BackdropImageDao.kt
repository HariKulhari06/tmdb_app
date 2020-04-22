package com.hari.tmdb.db.internal.daos

import androidx.room.Dao
import com.hari.tmdb.db.internal.entity.BackdropImageEntity

@Dao
abstract class BackdropImageDao : EntityDao<BackdropImageEntity>()