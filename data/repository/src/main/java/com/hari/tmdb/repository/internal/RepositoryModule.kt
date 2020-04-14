package com.hari.tmdb.repository.internal

import com.hari.tmdb.model.repository.MoviesRepository
import dagger.Binds
import dagger.Module

@Module(includes = [RepositoryModule.Providers::class])
internal abstract class RepositoryModule {

    @Binds
    abstract fun filtersRepository(impl: MoviesRepositoryImp): MoviesRepository

    @Module
    internal object Providers
}