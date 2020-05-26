package com.hari.tmdb.shows.internal.ui

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialFadeThrough
import com.hari.tmdb.di.PageScope
import com.hari.tmdb.ext.assistedActivityViewModels
import com.hari.tmdb.ext.assistedViewModels
import com.hari.tmdb.groupie.CarouselItemDecoration
import com.hari.tmdb.groupie.HeaderItemDecoration
import com.hari.tmdb.model.Show
import com.hari.tmdb.shows.R
import com.hari.tmdb.shows.databinding.MoviesMainFragmentBinding
import com.hari.tmdb.shows.internal.adapter.ShowAdapter
import com.hari.tmdb.shows.internal.items.CarouselGroup
import com.hari.tmdb.shows.internal.items.ItemTopTrendingBanner
import com.hari.tmdb.shows.internal.viewmodel.ShowsViewModel
import com.hari.tmdb.system.viewmodel.SystemViewModel
import com.hari.tmdb.ui.item.HeaderItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.databinding.GroupieViewHolder
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.coroutines.delay
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

    @Inject
    lateinit var itemTopTrendingBannerFactory: ItemTopTrendingBanner.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough.create(requireContext())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = MoviesMainFragmentBinding.bind(view)

        binding.recyclerViewShows.addItemDecoration(headerItemDecoration())
        val adapter = GroupAdapter<GroupieViewHolder<*>>()
        binding.recyclerViewShows.adapter = adapter

        showsViewModel.latestAiredShow.observe(viewLifecycleOwner, Observer { show: Show ->
            /* if(adapter.getItem(0) is ItemTopTrendingBanner){
                 adapter.removeGroupAtAdapterPosition(0)
                 adapter.add(0,createBanner(show))
             }else{
                 adapter.add(0,createBanner(show))
             }*/
        })

        val popularShowSection = Section(HeaderItem(titleStringResId = R.string.popular) {}).apply {
            setHideWhenEmpty(true)
        }
        popularShowSection.add(createCarousal(showsViewModel.popularShoePagedList))
        adapter.add(popularShowSection)

        val airingTodayShowSection =
            Section(HeaderItem(titleStringResId = R.string.airing_today) {}).apply {
                setHideWhenEmpty(
                    true
                )
            }
        airingTodayShowSection.add(createCarousal(showsViewModel.airingTodayShoePagedList))
        adapter.add(airingTodayShowSection)

        val onTVShowSection =
            Section(HeaderItem(titleStringResId = R.string.on_tv) {}).apply { setHideWhenEmpty(true) }
        onTVShowSection.add(createCarousal(showsViewModel.onTvShoePagedList))
        adapter.add(onTVShowSection)

        val topTreadingShowSection =
            Section(HeaderItem(titleStringResId = R.string.top_rated) {}).apply {
                setHideWhenEmpty(
                    true
                )
            }
        topTreadingShowSection.add(createCarousal(showsViewModel.topRatedShoePagedList))
        adapter.add(topTreadingShowSection)


        lifecycleScope.launchWhenResumed {
            delay(1000)
            showsViewModel.refresh()
        }
    }

    private fun createCarousal(showsPagedList: LiveData<PagedList<Show>>): CarouselGroup {
        val showAdapter = ShowAdapter()
        val carouselGroup = CarouselGroup(
            itemDecoration = CarouselItemDecoration(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_surface
                ),
                resources.getDimensionPixelSize(R.dimen.item_decoration_album),
                firstAndLastItemPadding = resources.getDimensionPixelSize(R.dimen.screen_space)
            ),
            adapter = showAdapter,
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
        )

        showsPagedList.observe(viewLifecycleOwner, Observer {
            showAdapter.submitList(it)
        })

        return carouselGroup
    }

    private fun headerItemDecoration(): HeaderItemDecoration {
        return HeaderItemDecoration(
            ContextCompat.getColor(
                requireContext(),
                R.color.color_surface
            ),
            resources.getDimensionPixelSize(R.dimen.screen_space)
        )
    }

    private fun createBanner(show: Show): ItemTopTrendingBanner {
        return itemTopTrendingBannerFactory.create(show)
    }

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