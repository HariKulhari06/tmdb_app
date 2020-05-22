package com.hari.tmdb.movie.item

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.hari.tmdb.model.Cast
import com.hari.tmdb.movie.R
import com.hari.tmdb.movie.databinding.ItemMovieCastingBinding
import com.hari.tmdb.movie.ui.MovieDetailFragmentDirections.Companion.actionMovieDetailToPeople
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.xwray.groupie.databinding.BindableItem

class MovieDetailCasting @AssistedInject constructor(
    @Assisted private val cast: Cast,
    private val lifecycleOwnerLiveData: LiveData<LifecycleOwner>
) : BindableItem<ItemMovieCastingBinding>(cast.id.hashCode().toLong()) {

    companion object {
        private const val TRANSITION_NAME_SUFFIX = "speaker"
    }

    override fun getLayout(): Int = R.layout.item_movie_casting

    override fun bind(viewBinding: ItemMovieCastingBinding, position: Int) {
        with(viewBinding) {
            casting = cast
            rootContainer.setOnClickListener {
                val extra = FragmentNavigatorExtras(
                    rootContainer to rootContainer.transitionName
                )

                rootContainer.findNavController().navigate(
                    actionMovieDetailToPeople(
                        title = cast.name,
                        peopleId = cast.id,
                        transitionNameSuffix = TRANSITION_NAME_SUFFIX
                    ),
                    extra
                )
            }

            rootContainer.transitionName = "${cast.id}-$TRANSITION_NAME_SUFFIX"
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            cast: Cast
        ): MovieDetailCasting
    }
}