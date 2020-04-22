package com.hari.tmdb.movie.item

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import coil.Coil
import coil.api.load
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
        with(viewBinding) {
            viewBinding.movie = movieData
            imageRequestDisposables += Coil.load(
                posterImage.context,
                "https://image.tmdb.org/t/p/w500/${movie?.posterPath}"
            ) {
                crossfade(true)
                placeholder(R.drawable.placeholder_72dp)
                lifecycle(lifecycleOwnerLiveData.value)
                target {
                    posterImage.setImageDrawable(it)
                }
            }

            imageRequestDisposables += Coil.load(
                productionImage.context,
                "https://image.tmdb.org/t/p/w300/${movie?.productionCompanyPath}"
            ) {
                crossfade(true)
                placeholder(R.drawable.placeholder_72dp)
                lifecycle(lifecycleOwnerLiveData.value)
                target {
                    productionImage.setImageDrawable(it)
                }
            }
            imageRequestDisposables.clear()
        }
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