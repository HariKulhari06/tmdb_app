package com.hari.tmdb.di

import android.content.Context
import com.hari.tmdb.db.internal.MoviesDataBase
import com.hari.tmdb.model.repository.MoviesRepository
import com.hari.tmdb.repository.RepositoryComponent
import com.uwetrottmann.tmdb2.DiscoverMovieBuilder
import com.uwetrottmann.tmdb2.services.GenresService
import com.uwetrottmann.tmdb2.services.MoviesService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object RepositoryComponentModule {
    @Provides
    @Singleton
    fun provideRepository(
        repositoryComponent: RepositoryComponent
    ): MoviesRepository {
        return repositoryComponent.moviesRepository()
    }

    @Provides
    @Singleton
    fun provideRepositoryComponent(
        context: Context,
        moviesDataBase: MoviesDataBase,
        genresService: GenresService,
        moviesService: MoviesService,
        discoverMovieBuilder: DiscoverMovieBuilder
    ): RepositoryComponent {

        return RepositoryComponent.factory()
            .create(
                context = context,
                moviesDataBase = moviesDataBase,
                genresService = genresService,
                moviesService = moviesService,
                discoverMovieBuilder = discoverMovieBuilder
            )

    }
}