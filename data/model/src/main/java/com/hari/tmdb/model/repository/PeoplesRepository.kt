package com.hari.tmdb.model.repository

import com.hari.tmdb.model.People
import kotlinx.coroutines.flow.Flow

interface PeoplesRepository {
    suspend fun refreshPeople(id: Int)
    suspend fun peopleObserver(id: Int): Flow<People>
}