package com.hari.tmdb.repository.internal

import com.hari.tmdb.db.internal.PeoplesDatabase
import com.hari.tmdb.ext.executeWithRetry
import com.hari.tmdb.ext.toResult
import com.hari.tmdb.log.Timber
import com.hari.tmdb.model.ErrorResult
import com.hari.tmdb.model.People
import com.hari.tmdb.model.Success
import com.hari.tmdb.model.repository.PeoplesRepository
import com.uwetrottmann.tmdb2.services.PeopleService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PeoplesRepositoryImp @Inject constructor(
    private val peoplesDatabase: PeoplesDatabase,
    private val peopleService: PeopleService
) : PeoplesRepository {

    override suspend fun refreshPeople(id: Int) {
        try {
            val result = peopleService.summary(id, null)
                .executeWithRetry()
                .toResult()

            when (result) {
                is Success -> {
                    peoplesDatabase.savePeople(result.data)
                }
                is ErrorResult -> {
                    Timber.error(result.throwable)
                }
            }
        } catch (e: Exception) {
            Timber.error(e)
        }


    }

    override suspend fun peopleObserver(id: Int): Flow<People> {
        return peoplesDatabase.people(id)
    }
}