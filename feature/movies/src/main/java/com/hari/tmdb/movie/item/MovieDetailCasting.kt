package com.hari.tmdb.movie.item

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import coil.Coil
import coil.api.load
import coil.request.RequestDisposable
import com.hari.tmdb.model.Cast
import com.hari.tmdb.movie.R
import com.hari.tmdb.movie.databinding.ItemMovieCastingBinding
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.GroupieViewHolder

class MovieDetailCasting @AssistedInject constructor(
    @Assisted private val cast: Cast,
    private val lifecycleOwnerLiveData: LiveData<LifecycleOwner>
) : BindableItem<ItemMovieCastingBinding>(cast.id.hashCode().toLong()) {

    private val imageRequestDisposables = mutableListOf<RequestDisposable>()

    companion object {
        private const val TRANSITION_NAME_SUFFIX = "movie"
    }

    override fun getLayout(): Int = R.layout.item_movie_casting

    override fun bind(viewBinding: ItemMovieCastingBinding, position: Int) {
        with(viewBinding) {
            imageRequestDisposables += Coil.load(
                profileImage.context,
                "https://image.tmdb.org/t/p/original/${cast.profilePath}"
            ) {
                crossfade(true)
                lifecycle(lifecycleOwnerLiveData.value)
                target {
                    profileImage.setImageDrawable(it)
                }
            }
            castName.text = cast.name
            castCharacter.text = cast.character
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
            cast: Cast
        ): MovieDetailCasting
    }
}