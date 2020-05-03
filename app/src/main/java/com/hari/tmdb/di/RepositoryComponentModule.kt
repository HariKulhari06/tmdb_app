package com.hari.tmdb.di

import android.content.Context
import com.hari.tmdb.db.internal.MoviesDataBase
import com.hari.tmdb.db.internal.PeoplesDatabase
import com.hari.tmdb.model.repository.AccountRepository
import com.hari.tmdb.model.repository.MoviesRepository
import com.hari.tmdb.model.repository.PeoplesRepository
import com.hari.tmdb.repository.RepositoryComponent
import com.uwetrottmann.tmdb2.DiscoverMovieBuilder
import com.uwetrottmann.tmdb2.services.AuthenticationService
import com.uwetrottmann.tmdb2.services.GenresService
import com.uwetrottmann.tmdb2.services.MoviesService
import com.uwetrottmann.tmdb2.services.PeopleService
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
    fun provideRepositoryComponent(
        context: Context,
        moviesDataBase: MoviesDataBase,
        peoplesDatabase: PeoplesDatabase,
        genresService: GenresService,
        moviesService: MoviesService,
        discoverMovieBuilder: DiscoverMovieBuilder,
        peopleService: PeopleService,
        authenticationService: AuthenticationService
    ): RepositoryComponent {

        return RepositoryComponent.factory()
            .create(
                context = context,
                moviesDataBase = moviesDataBase,
                peoplesDatabase = peoplesDatabase,
                genresService = genresService,
                moviesService = moviesService,
                discoverMovieBuilder = discoverMovieBuilder,
                personService = peopleService,
                authenticationService = authenticationService
            )

    }
}