package com.hari.tmdb.db.internal.daos

import com.hari.tmdb.db.internal.resultentities.EntryWithMovie
import com.hari.tmdb.model.MultipleEntry
import kotlinx.coroutines.flow.Flow

/**
 * This interface represents a DAO which contains entities which are part of a collective list for a given show.
 */
abstract class PairEntryDao<EC : MultipleEntry, LI : EntryWithMovie<EC>> : EntityDao<EC>() {
    abstract fun entries(movieId: Long): List<EC>
    abstract fun entriesWithMovies(movieId: Long): List<LI>
    abstract fun entriesWithMoviesObservable(movieId: Long): Flow<List<LI>>
    abstract suspend fun deleteWithMovieId(movieId: Long)
}
