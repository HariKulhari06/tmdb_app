package com.hari.tmdb.shows.internal.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hari.tmdb.model.Show
import com.hari.tmdb.shows.R
import com.hari.tmdb.shows.databinding.ItemShowPosterBinding
import com.hari.tmdb.shows.internal.ui.ShowsMainFragmentDirections.Companion.showsToShowDetail

class ShowAdapter : PagedListAdapter<Show, ShowAdapter.ShowViewHolder>(
    diffCallbacks
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        return ShowViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_show_poster,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        getItem(position)?.let { show ->
            holder.bind(show)
        }

    }

    class ShowViewHolder(private val binding: ItemShowPosterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(show: Show) {
            binding.show = show
            binding.imageViewPoster.setOnClickListener {
                val extra = FragmentNavigatorExtras(
                    binding.imageViewPoster to binding.imageViewPoster.transitionName
                )
                it.findNavController().navigate(showsToShowDetail(show.tmdbId), extra)
            }
            binding.imageViewPoster.transitionName = show.tmdbId.toString()
        }
    }


    companion object {
        val diffCallbacks = object : DiffUtil.ItemCallback<Show>() {
            override fun areItemsTheSame(oldItem: Show, newItem: Show): Boolean {
                return oldItem.tmdbId == newItem.tmdbId
            }

            override fun areContentsTheSame(oldItem: Show, newItem: Show): Boolean {
                return oldItem == newItem
            }

        }
    }


}