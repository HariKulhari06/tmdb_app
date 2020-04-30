package com.hari.tmdb.repository.internal

import com.hari.tmdb.model.repository.AccountRepository
import com.hari.tmdb.model.repository.MoviesRepository
import com.hari.tmdb.model.repository.PeoplesRepository
import dagger.Binds
import dagger.Module

@Module(includes = [RepositoryModule.Providers::class])
internal abstract class RepositoryModule {

    @Binds
    abstract fun movieRepository(impl: MoviesRepositoryImp): MoviesRepository

    @Binds
    abstract fun peopleRepository(impl: PeoplesRepositoryImp): PeoplesRepository

    @Binds
    abstract fun accountRepository(impl: AccountRepositoryImp): AccountRepository

    @Module
    internal object Providers
}