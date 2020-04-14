package com.hari.tmdb.movie.di

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_MovieAssistedInjectModule::class])
abstract class MovieAssistedInjectModule
