package com.hari.tmdb.repository.internal

import com.hari.tmdb.model.repository.*
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

    @Binds
    abstract fun searchRepository(impl: SearchRepositoryImp): SearchRepository

    @Binds
    abstract fun showsRepository(impl: ShowsRepositoryImp): ShowsRepository

    @Module
    internal object Providers
}