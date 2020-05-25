package com.hari.tmdb.shows.internal.items

import com.hari.tmdb.shows.R
import com.hari.tmdb.shows.databinding.ItemShowPosterBinding
import com.xwray.groupie.databinding.BindableItem

class ItemShowPoster : BindableItem<ItemShowPosterBinding>() {

    override fun getLayout() = R.layout.item_show_poster

    override fun bind(viewBinding: ItemShowPosterBinding, position: Int) {

    }
}