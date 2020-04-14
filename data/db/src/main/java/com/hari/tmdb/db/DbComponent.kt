package com.hari.tmdb.db

import android.content.Context
import com.hari.tmdb.db.internal.DbModule
import com.hari.tmdb.db.internal.MoviesDataBase
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
@Component(
    modules = [
        DbModule::class
    ]
)
interface DbComponent {
    fun moviesDataBase(): MoviesDataBase

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance coroutineContext: CoroutineContext
        ): DbComponent
    }

    companion object {
        fun factory(): Factory = DaggerDbComponent.factory()
    }

}
