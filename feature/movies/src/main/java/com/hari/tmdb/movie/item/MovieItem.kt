package com.hari.tmdb.movie.item

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import coil.Coil
import coil.api.load
import coil.request.RequestDisposable
import com.google.android.flexbox.FlexboxLayoutManager
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

    companion object {
        private const val TRANSITION_NAME_SUFFIX = "movie"
    }

    override fun getLayout(): Int = R.layout.movie_item

    override fun bind(viewBinding: MovieItemBinding, position: Int) {
        with(viewBinding) {
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

            val lp = root.layoutParams
            if (lp is FlexboxLayoutManager.LayoutParams) {
                lp.flexGrow = 1f
            }
            root.setOnClickListener {
                val extra = FragmentNavigatorExtras(
                    root to root.transitionName
                )
                root.findNavController().navigate(
                    actionMoviesToMovieDetail(movieId = movie.id, title = movie.title),
                    extra
                )
            }
            root.transitionName = TRANSITION_NAME_SUFFIX
            imageRequestDisposables.clear()
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