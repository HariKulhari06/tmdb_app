package com.hari.tmdb.search.item

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.hari.tmdb.model.Movie
import com.hari.tmdb.search.R
import com.hari.tmdb.search.databinding.ItemSearchBinding
import com.hari.tmdb.search.ui.SearchFragmentDirections.Companion.actionSearchToMovieDetail
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.xwray.groupie.databinding.BindableItem

class SearchItem @AssistedInject constructor(
    @Assisted private val movie: Movie,
    private val lifecycleOwnerLiveData: LiveData<LifecycleOwner>
) : BindableItem<ItemSearchBinding>(movie.id.hashCode().toLong()) {

    override fun getLayout(): Int = R.layout.item_search

    override fun bind(viewBinding: ItemSearchBinding, position: Int) {
        viewBinding.movie = movie
        viewBinding.imageViewPoster.setOnClickListener {
            val extra = FragmentNavigatorExtras(
                viewBinding.imageViewPoster to viewBinding.imageViewPoster.transitionName
            )
            viewBinding.imageViewPoster.findNavController().navigate(
                actionSearchToMovieDetail(movieId = movie.id, title = movie.title),
                extra
            )
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            movie: Movie
        ): SearchItem
    }
}