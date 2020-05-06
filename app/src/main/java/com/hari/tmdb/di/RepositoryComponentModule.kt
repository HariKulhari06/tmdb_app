package com.hari.tmdb.di

import android.content.Context
import com.hari.tmdb.db.internal.MoviesDataBase
import com.hari.tmdb.db.internal.PeoplesDatabase
import com.hari.tmdb.model.repository.AccountRepository
import com.hari.tmdb.model.repository.MoviesRepository
import com.hari.tmdb.model.repository.PeoplesRepository
import com.hari.tmdb.model.repository.SearchRepository
import com.hari.tmdb.repository.RepositoryComponent
import com.uwetrottmann.tmdb2.services.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object RepositoryComponentModule {
    @Provides
    @Singleton
    fun provideMoviesRepository(
        repositoryComponent: RepositoryComponent
    ): MoviesRepository {
        return repositoryComponent.moviesRepository()
    }

    @Provides
    @Singleton
    fun providePeopleRepository(
        repositoryComponent: RepositoryComponent
    ): PeoplesRepository {
        return repositoryComponent.peoplesRepository()
    }

    @Provides
    @Singleton
    fun provideAccountRepository(
        repositoryComponent: RepositoryComponent
    ): AccountRepository {
        return repositoryComponent.accountRepository()
    }


    @Provides
    @Singleton
    fun provideSearchRepository(
        repositoryComponent: RepositoryComponent
    ): SearchRepository {
        return repositoryComponent.searchRepository()
    }

    @Provides
    @Singleton
    fun provideRepositoryComponent(
        context: Context,
        moviesDataBase: MoviesDataBase,
        peoplesDatabase: PeoplesDatabase,
        genresService: GenresService,
        moviesService: MoviesService,
        discoverService: DiscoverService,
        peopleService: PeopleService,
        authenticationService: AuthenticationService,
        searchService: SearchService
    ): RepositoryComponent {

        return RepositoryComponent.factory()
            .create(
                context = context,
                moviesDataBase = moviesDataBase,
                peoplesDatabase = peoplesDatabase,
                genresService = genresService,
                moviesService = moviesService,
                discoverService = discoverService,
                personService = peopleService,
                authenticationService = authenticationService,
                searchService = searchService
            )

    }
}