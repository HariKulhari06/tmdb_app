package com.hari.tmdb.movie.item

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.hari.tmdb.model.Video
import com.hari.tmdb.movie.R
import com.hari.tmdb.movie.databinding.ItemMovieVideoBinding
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.xwray.groupie.databinding.BindableItem

class MovieDetailVideoItem @AssistedInject constructor(
    @Assisted private val video: Video,
    private val lifecycleOwnerLiveData: LiveData<LifecycleOwner>
) : BindableItem<ItemMovieVideoBinding>(video.id.hashCode().toLong()) {

    override fun getLayout(): Int = R.layout.item_movie_video

    override fun bind(viewBinding: ItemMovieVideoBinding, position: Int) {
        viewBinding.video = video
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            video: Video
        ): MovieDetailVideoItem
    }
}