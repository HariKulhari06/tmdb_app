package com.hari.tmdb.repository

import android.content.Context
import com.hari.tmdb.db.internal.MoviesDataBase
import com.hari.tmdb.db.internal.PeoplesDatabase
import com.hari.tmdb.model.repository.MoviesRepository
import com.hari.tmdb.model.repository.PeoplesRepository
import com.hari.tmdb.repository.internal.RepositoryModule
import com.uwetrottmann.tmdb2.DiscoverMovieBuilder
import com.uwetrottmann.tmdb2.services.GenresService
import com.uwetrottmann.tmdb2.services.MoviesService
import com.uwetrottmann.tmdb2.services.PeopleService
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


    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance moviesDataBase: MoviesDataBase,
            @BindsInstance peoplesDatabase: PeoplesDatabase,
            @BindsInstance genresService: GenresService,
            @BindsInstance moviesService: MoviesService,
            @BindsInstance discoverMovieBuilder: DiscoverMovieBuilder,
            @BindsInstance personService: PeopleService
        ): RepositoryComponent
    }

    companion object {
        fun factory(): Factory = DaggerRepositoryComponent.factory()
    }
}