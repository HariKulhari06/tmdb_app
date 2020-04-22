package com.hari.tmdb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.hari.tmdb.databinding.ActivityMainBinding
import com.hari.tmdb.di.PageScope
import com.hari.tmdb.ext.assistedActivityViewModels
import com.hari.tmdb.ext.getThemeColor
import com.hari.tmdb.ext.stringRes
import com.hari.tmdb.movie.di.MovieAssistedInjectModule
import com.hari.tmdb.movie.ui.MainMovieFragment
import com.hari.tmdb.movie.ui.MainMovieFragmentModule
import com.hari.tmdb.movie.ui.MovieDetailFragment
import com.hari.tmdb.movie.ui.MovieDetailFragmentModule
import com.hari.tmdb.system.viewmodel.SystemViewModel
import com.hari.tmdb.ui.PageConfiguration
import com.hari.tmdb.ui.widget.SystemUiManager
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject
import javax.inject.Provider

class MainActivity : AppCompatActivity(), HasAndroidInjector {
    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        )
    }

    @Inject
    lateinit var systemViewModelProvider: Provider<SystemViewModel>
    private val systemViewModel: SystemViewModel by assistedActivityViewModels {
        systemViewModelProvider.get()
    }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    private val navController: NavController by lazy {
        Navigation.findNavController(this, R.id.root_nav_host_fragment)
    }

    private val statusBarColors: SystemUiManager by lazy {
        SystemUiManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        setupNavigation()
        setupStatusBarColors()

        systemViewModel.errorLiveData.observe(this) { appError ->
            Snackbar
                .make(
                    findViewById(R.id.root_nav_host_fragment),
                    appError.stringRes(),
                    Snackbar.LENGTH_LONG
                )
                .show()
        }
    }

    private fun setupNavigation() {
        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.splash, R.id.login, R.id.main))
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            onDestinationChange(destination)
        }
    }

    private fun onDestinationChange(destination: NavDestination) {
        val config = PageConfiguration.getConfiguration(destination.id)

        statusBarColors.isIndigoBackground = config.isIndigoBackground
        binding.isIndigoBackground = config.isIndigoBackground
        binding.toolbar.isGone = config.hideToolbar
        binding.bottomNavigationView.isGone = config.hideBottomNavigationMenu

        if (!config.hasTitle) {
            supportActionBar?.title = ""
        }
        if (config.isShowLogoImage) {
            supportActionBar?.setLogo(null)
        } else {
            supportActionBar?.setLogo(null)
        }

        val iconTint = getThemeColor(
            if (config.isIndigoBackground) {
                R.attr.colorOnPrimary
            } else {
                R.attr.colorOnSurface
            }
        )
        if (destination.id != R.id.main)
            binding.toolbar.navigationIcon = if (config.isTopLevel) {
                AppCompatResources.getDrawable(this, R.drawable.ic_arrow_back_black_24dp)
            } else {
                AppCompatResources.getDrawable(this, R.drawable.ic_arrow_back_black_24dp)
            }.apply {
                this?.setTint(iconTint)
            }

    }

    private fun setupStatusBarColors() {
        statusBarColors.systemUiVisibility.distinctUntilChanged().observe(this) { visibility ->
            window.decorView.systemUiVisibility = visibility
        }
        statusBarColors.statusBarColor.distinctUntilChanged().observe(this) { color ->
            window.statusBarColor = color
        }
        statusBarColors.navigationBarColor.distinctUntilChanged().observe(this) { color ->
            window.navigationBarColor = color
        }
    }
}

@Module
abstract class MainActivityModule {
    @Binds
    abstract fun providesActivity(mainActivity: MainActivity): FragmentActivity


    @PageScope
    @ContributesAndroidInjector(
        modules = [MainMovieFragmentModule::class, MovieAssistedInjectModule::class]
    )
    abstract fun contributeMoviesFragment(): MainMovieFragment

    @PageScope
    @ContributesAndroidInjector(
        modules = [MovieDetailFragmentModule::class, MovieAssistedInjectModule::class]
    )
    abstract fun contributeMovieDetailFragment(): MovieDetailFragment


    @Module
    abstract class MainActivityBuilder {
        @ContributesAndroidInjector(modules = [MainActivityModule::class])
        abstract fun contributeMainActivity(): MainActivity
    }

}