package com.hari.tmdb.movie.item

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import coil.request.RequestDisposable
import com.google.android.material.chip.Chip
import com.hari.tmdb.model.Genre
import com.hari.tmdb.model.Movie
import com.hari.tmdb.movie.R
import com.hari.tmdb.movie.databinding.ItemMovieDetailAboutBinding
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.xwray.groupie.databinding.BindableItem

class MovieDetailAboutItem @AssistedInject constructor(
    @Assisted private val movie: Movie,
    private val lifecycleOwnerLiveData: LiveData<LifecycleOwner>
) : BindableItem<ItemMovieDetailAboutBinding>(movie.id.hashCode().toLong()) {

    private val imageRequestDisposables = mutableListOf<RequestDisposable>()


    override fun getLayout(): Int = R.layout.item_movie_detail_about

    @RequiresApi(Build.VERSION_CODES.M)
    override fun bind(viewBinding: ItemMovieDetailAboutBinding, position: Int) {
        with(viewBinding) {
            aboutTagline.text = movie.tagLine
            aboutText.text = movie.overview

            genreChipGroup.removeAllViews()
            movie.genres?.forEach { chip ->
                genreChipGroup.addView(createGenreChip(genreChipGroup.context, chip))
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun createGenreChip(context: Context, genre: Genre): Chip {
        return Chip(context).apply {
            text = genre.name
            chipBackgroundColor = context.getColorStateList(R.color.movies_detail_genre_background)
            isClickable = false
        }
    }


    @AssistedInject.Factory
    interface Factory {
        fun create(
            movie: Movie
        ): MovieDetailAboutItem
    }
}