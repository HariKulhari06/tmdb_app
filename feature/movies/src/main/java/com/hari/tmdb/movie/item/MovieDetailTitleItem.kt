package com.hari.tmdb.movie.item

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import coil.request.RequestDisposable
import com.hari.tmdb.model.Movie
import com.hari.tmdb.movie.R
import com.hari.tmdb.movie.databinding.ItemMovieDetailTitleBinding
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.GroupieViewHolder

class MovieDetailTitleItem @AssistedInject constructor(
    @Assisted private val movieData: Movie,
    private val lifecycleOwnerLiveData: LiveData<LifecycleOwner>
) : BindableItem<ItemMovieDetailTitleBinding>(movieData.id.hashCode().toLong()) {

    private val imageRequestDisposables = mutableListOf<RequestDisposable>()

    override fun getLayout(): Int = R.layout.item_movie_detail_title

    override fun bind(viewBinding: ItemMovieDetailTitleBinding, position: Int) {
        viewBinding.movie = movieData
    }

    override fun unbind(viewHolder: GroupieViewHolder<ItemMovieDetailTitleBinding>) {
        super.unbind(viewHolder)
        imageRequestDisposables.forEach { it.dispose() }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            movieData: Movie
        ): MovieDetailTitleItem
    }
}