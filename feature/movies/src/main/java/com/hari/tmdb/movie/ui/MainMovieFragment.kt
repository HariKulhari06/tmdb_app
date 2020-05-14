package com.hari.tmdb.movie.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialFadeThrough
import com.hari.tmdb.account.ui.MoviesPageFragmentArgs
import com.hari.tmdb.di.PageScope
import com.hari.tmdb.ext.assistedActivityViewModels
import com.hari.tmdb.model.MovieCategory
import com.hari.tmdb.movie.R
import com.hari.tmdb.movie.databinding.MainMovieFragmentBinding
import com.hari.tmdb.movie.ui.MainMovieFragmentDirections.Companion.actionMoviesToSearch
import com.hari.tmdb.movie.viewmodel.MoviesViewModel
import com.hari.tmdb.system.viewmodel.SystemViewModel
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject
import javax.inject.Provider

class MainMovieFragment : Fragment(R.layout.main_movie_fragment), HasAndroidInjector {

    @Inject
    lateinit var moviesViewModelProvider: Provider<MoviesViewModel>
    private val moviesViewModel: MoviesViewModel by assistedActivityViewModels {
        moviesViewModelProvider.get()
    }

    @Inject
    lateinit var systemViewModelProvider: Provider<SystemViewModel>
    private val systemViewModel: SystemViewModel by assistedActivityViewModels {
        systemViewModelProvider.get()
    }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        enterTransition = MaterialFadeThrough.create(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        val binding = MainMovieFragmentBinding.bind(view)
        setupMoviePager(binding)

    }

    private fun setupMoviePager(binding: MainMovieFragmentBinding) {
        val tabTitle = resources.getStringArray(R.array.movie_main_tabs)
        val tabLayoutMediator = TabLayoutMediator(
            binding.tabLayout,
            binding.moviesViewPager
        ) { tab, position ->
            tab.text = tabTitle[position]
        }

        binding.moviesViewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = tabTitle.size
            override fun createFragment(position: Int): Fragment =
                if (position == 0) DiscoverMoviesFragment() else MoviesPageFragment.newInstance(
                    MoviesPageFragmentArgs(MovieCategory.movieCategory(position))
                )
        }

        tabLayoutMediator.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.movies_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                findNavController().navigate(actionMoviesToSearch())
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

@Module
abstract class MainMovieFragmentModule {

    @ContributesAndroidInjector(modules = [DiscoverMovieFragmentModule::class])
    abstract fun contributeDiscoverPageFragment(): DiscoverMoviesFragment

    @ContributesAndroidInjector(modules = [MoviePageFragmentModule::class])
    abstract fun contributeMoviesPageFragment(): MoviesPageFragment

    companion object {
        @PageScope
        @Provides
        fun providesLifecycleOwnerLiveData(
            mainSessionsFragment: MainMovieFragment
        ): LiveData<LifecycleOwner> {
            return mainSessionsFragment.viewLifecycleOwnerLiveData
        }
    }
}
