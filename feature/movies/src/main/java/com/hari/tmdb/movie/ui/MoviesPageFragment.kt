package com.hari.tmdb.movie.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.hari.tmdb.account.ui.MoviesPageFragmentArgs
import com.hari.tmdb.di.PageScope
import com.hari.tmdb.ext.assistedActivityViewModels
import com.hari.tmdb.ext.assistedViewModels
import com.hari.tmdb.movie.R
import com.hari.tmdb.movie.databinding.MoviesPageFragmentBinding
import com.hari.tmdb.movie.item.MovieItem
import com.hari.tmdb.movie.viewmodel.MoviePageViewModel
import com.hari.tmdb.system.viewmodel.SystemViewModel
import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.databinding.GroupieViewHolder
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject
import javax.inject.Provider

class MoviesPageFragment : Fragment(R.layout.movies_page_fragment), HasAndroidInjector {

    @Inject
    lateinit var movieItemFactory: MovieItem.Factory

    @Inject
    lateinit var systemViewModelFactory: Provider<SystemViewModel>
    private val systemViewModel by assistedActivityViewModels {
        systemViewModelFactory.get()
    }

    @Inject
    lateinit var moviesPageViewModelFactory: MoviePageViewModel.Factory
    private val moviesPageViewModel by assistedViewModels {
        moviesPageViewModelFactory.create(args.page)
    }

    private val args: MoviesPageFragmentArgs by lazy {
        MoviesPageFragmentArgs.fromBundle(arguments ?: Bundle())
    }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = MoviesPageFragmentBinding.bind(view)
        val adapter = GroupAdapter<GroupieViewHolder<*>>()
        binding.moviesRecycler.adapter = adapter

        moviesPageViewModel.ui.observe(viewLifecycleOwner, Observer { uiModel ->
            binding.progressBar.isVisible = uiModel.isLoading
            uiModel.movies?.let { movies ->
                /*  setUpMoviesView(
                      binding,
                      adapter,
                      moviesMOvie
                  )*/

                val items = mutableListOf<Group>()
                items += movies.map {
                    movieItemFactory.create(it)
                }
                adapter.update(items)
            }
        })

        binding.swipeRefreshLayout.setOnRefreshListener { moviesPageViewModel.refresh() }
    }

    /*  private fun setUpMoviesView(
          binding: MoviesPageFragmentBinding,
          adapter: GroupAdapter<GroupieViewHolder<*>>,
          movies: List<Movie>
      ) {
          val items = mutableListOf<Group>()
          items += movies.map {
              movieItemFactory.create(it)
          }
          adapter.update(items)
      }*/


    companion object {
        fun newInstance(args: MoviesPageFragmentArgs): MoviesPageFragment {
            return MoviesPageFragment().apply {
                arguments = args.toBundle()
            }
        }
    }
}

@Module
abstract class MoviePageFragmentModule {
    companion object {
        @PageScope
        @Provides
        fun providesLifecycleOwnerLiveData(
            moviesPageFragment: MoviesPageFragment
        ): LiveData<LifecycleOwner> {
            return moviesPageFragment.viewLifecycleOwnerLiveData
        }
    }
}


