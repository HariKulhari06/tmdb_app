package com.hari.tmdb.movie.item

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import coil.request.RequestDisposable
import com.hari.tmdb.model.Movie
import com.hari.tmdb.movie.R
import com.hari.tmdb.movie.databinding.MovieItemBinding
import com.hari.tmdb.movie.ui.MainMovieFragmentDirections.Companion.actionMoviesToMovieDetail
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.GroupieViewHolder

class MovieItem @AssistedInject constructor(
    @Assisted private val movie: Movie,
    private val lifecycleOwnerLiveData: LiveData<LifecycleOwner>
) : BindableItem<MovieItemBinding>(movie.id.hashCode().toLong()) {

    private val imageRequestDisposables = mutableListOf<RequestDisposable>()

    override fun getLayout(): Int = R.layout.movie_item

    override fun bind(viewBinding: MovieItemBinding, position: Int) {
        viewBinding.movie = movie
        viewBinding.imageViewPoster.transitionName = movie.id.toString()
        viewBinding.imageViewPoster.setOnClickListener {
            val extra = FragmentNavigatorExtras(
                viewBinding.imageViewPoster to viewBinding.imageViewPoster.transitionName
            )
            viewBinding.imageViewPoster.findNavController().navigate(
                actionMoviesToMovieDetail(movieId = movie.id, title = movie.title),
                extra
            )
        }
    }

    override fun unbind(viewHolder: GroupieViewHolder<MovieItemBinding>) {
        super.unbind(viewHolder)
        imageRequestDisposables.forEach { it.dispose() }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            movie: Movie
        ): MovieItem
    }
}