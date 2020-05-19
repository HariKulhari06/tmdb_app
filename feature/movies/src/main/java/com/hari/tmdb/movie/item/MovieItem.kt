package com.hari.tmdb.movie.item

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import coil.Coil
import coil.api.load
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
        with(viewBinding) {
            imageViewPoster.setOnClickListener {
                val extra = FragmentNavigatorExtras(
                    imageViewPoster to imageViewPoster.transitionName
                )
                root.findNavController().navigate(
                    actionMoviesToMovieDetail(movieId = movie.id, title = movie.title),
                    extra
                )
            }

            imageViewPoster.transitionName = movie.id.toString()
            imageRequestDisposables.clear()

            imageViewPoster.setImageDrawable(null)
            imageRequestDisposables += Coil.load(
                imageViewPoster.context,
                "https://image.tmdb.org/t/p/w500/${movie.posterPath}"
            ) {
                crossfade(true)
                placeholder(R.drawable.placeholder_72dp)
                lifecycle(lifecycleOwnerLiveData.value)
                target {
                    imageViewPoster.setImageDrawable(it)
                }
            }
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