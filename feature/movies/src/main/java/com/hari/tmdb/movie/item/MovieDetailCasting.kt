package com.hari.tmdb.movie.item

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import coil.Coil
import coil.api.load
import coil.request.RequestDisposable
import coil.transform.CircleCropTransformation
import com.hari.tmdb.model.Movie
import com.hari.tmdb.movie.R
import com.hari.tmdb.movie.databinding.ItemMovieCastingBinding
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.GroupieViewHolder

class MovieDetailCasting @AssistedInject constructor(
    @Assisted private val movie: Movie,
    private val lifecycleOwnerLiveData: LiveData<LifecycleOwner>
) : BindableItem<ItemMovieCastingBinding>(movie.id.hashCode().toLong()) {

    private val imageRequestDisposables = mutableListOf<RequestDisposable>()

    companion object {
        private const val TRANSITION_NAME_SUFFIX = "movie"
    }

    override fun getLayout(): Int = R.layout.item_movie_casting

    override fun bind(viewBinding: ItemMovieCastingBinding, position: Int) {
        with(viewBinding) {
            imageRequestDisposables += Coil.load(
                castingImage.context,
                "https://image.tmdb.org/t/p/original/vHqpFuguBugZZfXIyjsb4tOAXve.jpg"
            ) {
                crossfade(true)
                transformations(CircleCropTransformation())
                lifecycle(lifecycleOwnerLiveData.value)
                target {
                    castingImage.setImageDrawable(it)
                }
            }
            imageRequestDisposables.clear()
        }
    }

    override fun unbind(viewHolder: GroupieViewHolder<ItemMovieCastingBinding>) {
        super.unbind(viewHolder)
        imageRequestDisposables.forEach { it.dispose() }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            movie: Movie
        ): MovieDetailCasting
    }
}