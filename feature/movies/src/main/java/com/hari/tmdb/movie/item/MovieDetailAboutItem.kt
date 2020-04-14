package com.hari.tmdb.movie.item

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import coil.request.RequestDisposable
import com.hari.tmdb.model.Movie
import com.hari.tmdb.movie.R
import com.hari.tmdb.movie.databinding.ItemMovieDetailAboutBinding
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.GroupieViewHolder

class MovieDetailAboutItem @AssistedInject constructor(
    @Assisted private val movie: Movie,
    private val lifecycleOwnerLiveData: LiveData<LifecycleOwner>
) : BindableItem<ItemMovieDetailAboutBinding>(movie.id.hashCode().toLong()) {

    private val imageRequestDisposables = mutableListOf<RequestDisposable>()

    companion object {
        private const val TRANSITION_NAME_SUFFIX = "movie"
    }

    override fun getLayout(): Int = R.layout.item_movie_detail_about

    override fun bind(viewBinding: ItemMovieDetailAboutBinding, position: Int) {
        with(viewBinding) {

            imageRequestDisposables.clear()
        }
    }

    override fun unbind(viewHolder: GroupieViewHolder<ItemMovieDetailAboutBinding>) {
        super.unbind(viewHolder)
        imageRequestDisposables.forEach { it.dispose() }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            movie: Movie
        ): MovieDetailAboutItem
    }
}