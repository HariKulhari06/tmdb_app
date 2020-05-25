package com.hari.tmdb.shows.internal.ui

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialFadeThrough
import com.hari.tmdb.groupie.CarouselItemDecoration
import com.hari.tmdb.groupie.HeaderItemDecoration
import com.hari.tmdb.shows.R
import com.hari.tmdb.shows.databinding.MoviesMainFragmentBinding
import com.hari.tmdb.shows.internal.items.ItemShowPoster
import com.hari.tmdb.shows.internal.items.ItemTopTrendingBanner
import com.hari.tmdb.ui.item.CarouselGroup
import com.hari.tmdb.ui.item.HeaderItem
import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.databinding.GroupieViewHolder

class ShowsMainFragment : Fragment(R.layout.movies_main_fragment) {

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

        addItems(adapter)
    }

    private fun addItems(adapter: GroupAdapter<GroupieViewHolder<*>>) {
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
        adapter.add(section)
    }


}