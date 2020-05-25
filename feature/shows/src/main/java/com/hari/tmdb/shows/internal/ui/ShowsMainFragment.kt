package com.hari.tmdb.shows.internal.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.transition.MaterialFadeThrough
import com.hari.tmdb.di.PageScope
import com.hari.tmdb.ext.assistedActivityViewModels
import com.hari.tmdb.ext.assistedViewModels
import com.hari.tmdb.groupie.HeaderItemDecoration
import com.hari.tmdb.shows.R
import com.hari.tmdb.shows.databinding.MoviesMainFragmentBinding
import com.hari.tmdb.shows.internal.items.ItemTopTrendingBanner
import com.hari.tmdb.shows.internal.viewmodel.ShowsViewModel
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

class ShowsMainFragment : Fragment(R.layout.movies_main_fragment), HasAndroidInjector {

    @Inject
    lateinit var showsViewModelProvider: Provider<ShowsViewModel>
    private val showsViewModel: ShowsViewModel by assistedViewModels {
        showsViewModelProvider.get()
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
        enterTransition = MaterialFadeThrough.create(requireContext())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = MoviesMainFragmentBinding.bind(view)

        binding.recyclerViewShows.addItemDecoration(
            HeaderItemDecoration(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_surface
                ),
                resources.getDimensionPixelSize(R.dimen.screen_space)
            )
        )
        val adapter = GroupAdapter<GroupieViewHolder<*>>()
        binding.recyclerViewShows.adapter = adapter

        val items = mutableListOf<Group>()
        items += ItemTopTrendingBanner()

        adapter.update(items)

        showsViewModel.popularShoePagedList.observe(viewLifecycleOwner, Observer {
            Log.e("onViewCreated: ", "${it.size}")
        })

    }

    /*  private fun addItems(): Section {
          val section =
              Section(HeaderItem(titleStringResId = R.string.popular) {})
          section.setHideWhenEmpty(true)

          val sectionAdapter = GroupAdapter<GroupieViewHolder<*>>()
          for (i in 1..20) {
              sectionAdapter.add(ItemShowPoster())
          }

          section.add(
              CarouselGroup(
                  itemDecoration = CarouselItemDecoration(
                      ContextCompat.getColor(
                          requireContext(),
                          R.color.color_surface
                      ),
                      resources.getDimensionPixelSize(R.dimen.item_decoration_album),
                      firstAndLastItemPadding = resources.getDimensionPixelSize(R.dimen.screen_space)
                  ),
                  adapter = sectionAdapter,
                  layoutManager = LinearLayoutManager(requireContext()).apply {
                      orientation = LinearLayoutManager.HORIZONTAL
                  }
              )
          )
         return section
      }*/

}

@Module
abstract class ShowsMainFragmentModule {

    companion object {
        @PageScope
        @Provides
        fun providesLifecycleOwnerLiveData(
            showsMainFragment: ShowsMainFragment
        ): LiveData<LifecycleOwner> {
            return showsMainFragment.viewLifecycleOwnerLiveData
        }
    }
}