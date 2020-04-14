package com.hari.tmdb

import com.hari.tmdb.di.AppComponent
import com.hari.tmdb.di.AppComponentHolder
import com.hari.tmdb.di.createAppComponent
import com.hari.tmdb.initializer.AppInitializers
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Inject

open class App : DaggerApplication(), AppComponentHolder {

    override val appComponent: AppComponent by lazy {
        createAppComponent()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return appComponent
    }

    @Inject
    lateinit var initializers: AppInitializers

    override fun onCreate() {
        super.onCreate()
        initializers.initialize(this)
    }

}