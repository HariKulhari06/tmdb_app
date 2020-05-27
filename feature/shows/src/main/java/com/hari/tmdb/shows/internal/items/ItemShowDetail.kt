package com.hari.tmdb.shows.internal.items

import com.hari.tmdb.shows.R
import com.hari.tmdb.shows.databinding.ItemShowDetailBinding
import com.xwray.groupie.databinding.BindableItem

class ItemShowDetail : BindableItem<ItemShowDetailBinding>() {

    override fun getLayout() = R.layout.item_show_detail

    override fun bind(viewBinding: ItemShowDetailBinding, position: Int) {
    }

}