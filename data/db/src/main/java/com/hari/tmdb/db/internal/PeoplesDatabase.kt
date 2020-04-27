package com.hari.tmdb.db.internal

import com.hari.tmdb.db.internal.entity.PeopleEntityImp
import com.hari.tmdb.model.People
import com.uwetrottmann.tmdb2.entities.Person
import kotlinx.coroutines.flow.Flow

interface PeoplesDatabase {
    suspend fun insertPeoples(peoples: List<PeopleEntityImp>)
    suspend fun people(id: Int): Flow<People>
    suspend fun savePeople(data: Person)
}