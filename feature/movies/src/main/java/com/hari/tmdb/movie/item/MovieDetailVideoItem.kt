package com.hari.tmdb.movie.item

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import coil.Coil
import coil.api.load
import coil.request.RequestDisposable
import com.hari.tmdb.model.Video
import com.hari.tmdb.movie.R
import com.hari.tmdb.movie.databinding.ItemMovieVideoBinding
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.GroupieViewHolder

class MovieDetailVideoItem @AssistedInject constructor(
    @Assisted private val video: Video,
    private val lifecycleOwnerLiveData: LiveData<LifecycleOwner>
) : BindableItem<ItemMovieVideoBinding>(video.id.hashCode().toLong()) {

    private val imageRequestDisposables = mutableListOf<RequestDisposable>()


    override fun getLayout(): Int = R.layout.item_movie_video

    override fun bind(viewBinding: ItemMovieVideoBinding, position: Int) {
        with(viewBinding) {
            imageRequestDisposables += Coil.load(
                videoThumbnail.context,
                "https://img.youtube.com/vi/${video.key}/maxresdefault.jpg"
            ) {
                crossfade(true)
                lifecycle(lifecycleOwnerLiveData.value)
                target {
                    videoThumbnail.setImageDrawable(it)
                }
            }
            root.setOnClickListener {

            }
            imageRequestDisposables.clear()
        }
    }

    override fun unbind(viewHolder: GroupieViewHolder<ItemMovieVideoBinding>) {
        super.unbind(viewHolder)
        imageRequestDisposables.forEach { it.dispose() }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            video: Video
        ): MovieDetailVideoItem
    }
}