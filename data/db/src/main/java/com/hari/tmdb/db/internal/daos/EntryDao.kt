package com.hari.tmdb.db.internal.daos


import com.hari.tmdb.db.internal.resultentities.EntryWithMovie
import com.hari.tmdb.model.Entry

/**
 * This interface represents a DAO which contains entities which are part of a single collective list.
 */
abstract class EntryDao<EC : Entry, LI : EntryWithMovie<EC>> : EntityDao<EC>() {
    abstract suspend fun deleteAll()
}
