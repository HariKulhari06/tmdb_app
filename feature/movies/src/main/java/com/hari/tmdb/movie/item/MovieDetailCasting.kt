package com.hari.tmdb.movie.item

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import coil.Coil
import coil.api.load
import coil.request.RequestDisposable
import com.hari.tmdb.model.Cast
import com.hari.tmdb.movie.R
import com.hari.tmdb.movie.databinding.ItemMovieCastingBinding
import com.hari.tmdb.movie.ui.MovieDetailFragmentDirections.Companion.actionMovieDetailToPeople
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
        private const val TRANSITION_NAME_SUFFIX = "speaker"
    }

    override fun getLayout(): Int = R.layout.item_movie_casting

    override fun bind(viewBinding: ItemMovieCastingBinding, position: Int) {


        with(viewBinding) {
            root.setOnClickListener {
                val extra = FragmentNavigatorExtras(
                    root to root.transitionName
                )

                root.findNavController().navigate(
                    actionMovieDetailToPeople(
                        title = cast.name,
                        peopleId = cast.id,
                        transitionNameSuffix = TRANSITION_NAME_SUFFIX
                    ),
                    extra
                )
            }
            castName.text = cast.name
            castCharacter.text = cast.character
            root.transitionName = "${cast.id}-$TRANSITION_NAME_SUFFIX"

            imageRequestDisposables.clear()

            profileImage.setImageDrawable(null)
            imageRequestDisposables += Coil.load(
                profileImage.context,
                "https://image.tmdb.org/t/p/w500/${cast.profilePath}"
            ) {
                crossfade(true)
                lifecycle(lifecycleOwnerLiveData.value)
                target {
                    profileImage.setImageDrawable(it)
                }
            }
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