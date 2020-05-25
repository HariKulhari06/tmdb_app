package com.hari.tmdb.shows.internal.di

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_ShowAssistedInjectModule::class])
abstract class ShowAssistedInjectModule
