package com.hari.tmdb.db.internal.daos

import androidx.room.*
import com.hari.tmdb.db.internal.entity.PeopleEntityImp
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PeopleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: PeopleEntityImp): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(entities: List<PeopleEntityImp>)

    @Update
    abstract suspend fun update(entity: PeopleEntityImp)

    @Delete
    abstract suspend fun deleteEntity(entity: PeopleEntityImp): Int

    @Transaction
    open suspend fun withTransaction(tx: suspend () -> Unit) = tx()

    @Query("SELECT * FROM peoples")
    abstract fun peoples(): Flow<List<PeopleEntityImp>>

    @Query("SELECT * FROM peoples WHERE id =:id")
    abstract fun people(id: Int): Flow<PeopleEntityImp>

    @Query("DELETE FROM peoples")
    abstract fun deleteAll()
}