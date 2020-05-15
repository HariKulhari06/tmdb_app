package com.hari.tmdb.movie.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.MergeAdapter
import com.hari.tmdb.account.ui.MoviesPageFragmentArgs
import com.hari.tmdb.di.PageScope
import com.hari.tmdb.ext.assistedActivityViewModels
import com.hari.tmdb.ext.assistedViewModels
import com.hari.tmdb.groupie.ItemDecorationAlbumColumns
import com.hari.tmdb.movie.R
import com.hari.tmdb.movie.adapter.LoadingStateAdapter
import com.hari.tmdb.movie.adapter.MoviesAdapter
import com.hari.tmdb.movie.databinding.MoviesPageFragmentBinding
import com.hari.tmdb.movie.item.MovieItem
import com.hari.tmdb.movie.viewmodel.MoviePageViewModel
import com.hari.tmdb.system.viewmodel.SystemViewModel
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import org.jetbrains.annotations.NotNull
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
        moviesPageViewModelFactory.create(args.category)
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

        initMoviesAdapter(binding)
        setUpSwipeToRefresh(binding)
    }

    private fun setUpSwipeToRefresh(binding: @NotNull MoviesPageFragmentBinding) {
        binding.swipeRefreshLayout.setOnRefreshListener { moviesPageViewModel.refresh() }
        moviesPageViewModel.refreshState.observe(viewLifecycleOwner, Observer { refreshState ->
            binding.swipeRefreshLayout.isRefreshing = refreshState.isLoading
        })
    }

    private fun initMoviesAdapter(binding: @NotNull MoviesPageFragmentBinding) {
        val loadingAdapter = LoadingStateAdapter { moviesPageViewModel.retry() }
        val pagedAdapter = MoviesAdapter()

        val mergeAdapter = MergeAdapter(pagedAdapter, loadingAdapter)

        val layoutManager = GridLayoutManager(requireContext(), 3)
        /*  layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
              override fun getSpanSize(position: Int): Int {
                  return if (loadingAdapter.hasExtraRow())
                      3
                  else
                      1
              }
          }*/

        binding.moviesRecycler.layoutManager = layoutManager
        binding.moviesRecycler.adapter = mergeAdapter
        binding.moviesRecycler.addItemDecoration(
            ItemDecorationAlbumColumns(
                resources.getDimensionPixelSize(R.dimen.item_decoration_album),
                3
            )
        )

        moviesPageViewModel.movies.observe(viewLifecycleOwner, Observer {
            pagedAdapter.submitList(it)
        })

        moviesPageViewModel.loadingState.observe(viewLifecycleOwner, Observer { loadingState ->
            loadingAdapter.setNetworkState(loadingState)
        })
    }


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


