package com.hari.tmdb.movie.item

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.hari.tmdb.model.Movie
import com.hari.tmdb.movie.R
import com.hari.tmdb.movie.databinding.ItemMovieDetailTitleBinding
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.xwray.groupie.databinding.BindableItem

class MovieDetailTitleItem @AssistedInject constructor(
    @Assisted private val movieData: Movie,
    private val lifecycleOwnerLiveData: LiveData<LifecycleOwner>
) : BindableItem<ItemMovieDetailTitleBinding>(movieData.id.hashCode().toLong()) {

    override fun getLayout(): Int = R.layout.item_movie_detail_title

    override fun bind(viewBinding: ItemMovieDetailTitleBinding, position: Int) {
        viewBinding.movie = movieData
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            movieData: Movie
        ): MovieDetailTitleItem
    }
}