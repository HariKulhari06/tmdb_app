package com.hari.tmdb.movie.item

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.hari.tmdb.model.Movie
import com.hari.tmdb.movie.R
import com.hari.tmdb.movie.databinding.ItemMovieRelatedBinding
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.xwray.groupie.databinding.BindableItem

class MovieDetailRelated @AssistedInject constructor(
    @Assisted private val movie: Movie,
    private val lifecycleOwnerLiveData: LiveData<LifecycleOwner>
) : BindableItem<ItemMovieRelatedBinding>(movie.id.hashCode().toLong()) {

    val first = false

    companion object {
        private const val TRANSITION_NAME_SUFFIX = "movie"
    }

    override fun getLayout(): Int = R.layout.item_movie_related

    override fun bind(viewBinding: ItemMovieRelatedBinding, position: Int) {

    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            movie: Movie
        ): MovieDetailRelated
    }
}