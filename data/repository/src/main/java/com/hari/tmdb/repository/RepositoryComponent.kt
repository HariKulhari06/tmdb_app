package com.hari.tmdb.repository

import android.content.Context
import com.hari.tmdb.db.internal.MoviesDataBase
import com.hari.tmdb.db.internal.PeoplesDatabase
import com.hari.tmdb.db.internal.ShowsDatabase
import com.hari.tmdb.model.repository.*
import com.hari.tmdb.repository.internal.RepositoryModule
import com.uwetrottmann.tmdb2.services.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RepositoryModule::class
    ]
)
interface RepositoryComponent {
    fun moviesRepository(): MoviesRepository
    fun peoplesRepository(): PeoplesRepository
    fun accountRepository(): AccountRepository
    fun searchRepository(): SearchRepository
    fun showsRepository(): ShowsRepository


    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance moviesDataBase: MoviesDataBase,
            @BindsInstance peoplesDatabase: PeoplesDatabase,
            @BindsInstance genresService: GenresService,
            @BindsInstance moviesService: MoviesService,
            @BindsInstance discoverService: DiscoverService,
            @BindsInstance personService: PeopleService,
            @BindsInstance authenticationService: AuthenticationService,
            @BindsInstance searchService: SearchService,
            @BindsInstance tvService: TvService,
            @BindsInstance showsDatabase: ShowsDatabase
        ): RepositoryComponent
    }

    companion object {
        fun factory(): Factory = DaggerRepositoryComponent.factory()
    }
}