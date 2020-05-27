package com.hari.tmdb.shows.internal.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialFadeThrough
import com.hari.tmdb.shows.R
import com.hari.tmdb.shows.databinding.ShowDetailFragmentBinding
import com.hari.tmdb.shows.internal.items.ItemShowDetail
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.databinding.GroupieViewHolder

class ShowDetailFragment : Fragment(R.layout.show_detail_fragment) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough.create(requireContext())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = ShowDetailFragmentBinding.bind(view)

        val adapter = GroupAdapter<GroupieViewHolder<*>>()
        adapter.add(ItemShowDetail())

        binding.recyclerViewShowDetail.adapter = adapter


    }

}