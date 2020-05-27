package com.hari.tmdb.shows.internal.items

import com.hari.tmdb.shows.R
import com.hari.tmdb.shows.databinding.ItemGenreCardBinding
import com.xwray.groupie.databinding.BindableItem

class ItemGenreCard : BindableItem<ItemGenreCardBinding>() {

    init {
        extras[INSET_TYPE_KEY] = INSET
    }

    override fun getLayout() = R.layout.item_genre_card

    override fun bind(viewBinding: ItemGenreCardBinding, position: Int) {
    }

    override fun getSpanSize(spanCount: Int, position: Int): Int {
        return spanCount / 3
    }

    companion object {
        const val INSET_TYPE_KEY = "inset_type"
        const val FULL_BLEED = "full_bleed"
        const val INSET = "inset"
    }
}