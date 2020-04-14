package com.hari.tmdb.di

import android.app.Application
import com.hari.tmdb.App
import com.hari.tmdb.MainActivityModule
import com.hari.tmdb.data.api.TmdbApiModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        AndroidInjectionModule::class,
        MainActivityModule.MainActivityBuilder::class,
        DbComponentModule::class,
        TmdbApiModule::class,
        RepositoryComponentModule::class
    ]
)
interface AppComponent : AndroidInjector<App>, AppComponentInterface {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }

    override fun inject(app: App)
}

fun Application.createAppComponent() = DaggerAppComponent.factory().create(this)