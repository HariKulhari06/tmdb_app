package com.hari.tmdb.search.di

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [AssistedInject_SearchAssistedInjectModule::class])
abstract class SearchAssistedInjectModule
