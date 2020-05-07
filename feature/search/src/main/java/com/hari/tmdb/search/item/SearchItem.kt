package com.hari.tmdb.search.item

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import coil.Coil
import coil.api.load
import coil.request.RequestDisposable
import com.hari.tmdb.model.Movie
import com.hari.tmdb.search.R
import com.hari.tmdb.search.databinding.ItemSearchBinding
import com.hari.tmdb.search.ui.SearchFragmentDirections.Companion.actionSearchToMovieDetail
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.GroupieViewHolder

class SearchItem @AssistedInject constructor(
    @Assisted private val movie: Movie,
    private val lifecycleOwnerLiveData: LiveData<LifecycleOwner>
) : BindableItem<ItemSearchBinding>(movie.id.hashCode().toLong()) {

    private val imageRequestDisposables = mutableListOf<RequestDisposable>()

    companion object {
        private const val TRANSITION_NAME_SUFFIX = "movie"
    }

    override fun getLayout(): Int = R.layout.item_search

    override fun bind(viewBinding: ItemSearchBinding, position: Int) {
        with(viewBinding) {
            imageViewPoster.setOnClickListener {
                val extra = FragmentNavigatorExtras(
                    imageViewPoster to imageViewPoster.transitionName
                )
                root.findNavController().navigate(
                    actionSearchToMovieDetail(movieId = movie.id, title = movie.title),
                    extra
                )
            }

            imageViewPoster.transitionName = TRANSITION_NAME_SUFFIX
            // imageRequestDisposables.clear()

            imageRequestDisposables += Coil.load(
                imageViewPoster.context,
                "https://image.tmdb.org/t/p/w185/${movie.posterPath}"
            ) {
                crossfade(true)
                placeholder(R.drawable.placeholder_72dp)
                error(R.drawable.placeholder_72dp)
                lifecycle(lifecycleOwnerLiveData.value)
                target {
                    imageViewPoster.setImageDrawable(it)
                }
            }
        }
    }

    override fun unbind(viewHolder: GroupieViewHolder<ItemSearchBinding>) {
        super.unbind(viewHolder)
        imageRequestDisposables.forEach { it.dispose() }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            movie: Movie
        ): SearchItem
    }
}