package com.hari.tmdb.data.api

import com.uwetrottmann.tmdb2.Tmdb
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [TmdbServiceModule::class])
class TmdbApiModule {
    @Provides
    @Singleton
    fun provideTmdb(
        @Named("tmdb-api") clientId: String
    ): Tmdb {
        return object : Tmdb(clientId) {
            override fun setOkHttpClientDefaults(builder: OkHttpClient.Builder) {
                super.setOkHttpClientDefaults(builder)
                builder.apply {
                    connectTimeout(20, TimeUnit.SECONDS)
                    readTimeout(20, TimeUnit.SECONDS)
                    writeTimeout(20, TimeUnit.SECONDS)
                    val loggingInterceptor = HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                    addInterceptor(loggingInterceptor)
                }
            }
        }
    }
}

@Module
class TmdbServiceModule {
    @Provides
    fun provideTmdbMoviesService(tmdb: Tmdb) = tmdb.moviesService()

    @Provides
    fun provideTmdbMoviesDiscoverService(tmdb: Tmdb) = tmdb.discoverMovie()

    @Provides
    fun provideTmdbGenreService(tmdb: Tmdb) = tmdb.genreService()

    @Provides
    fun provideTmdbPeoplesService(tmdb: Tmdb) = tmdb.personService()
}