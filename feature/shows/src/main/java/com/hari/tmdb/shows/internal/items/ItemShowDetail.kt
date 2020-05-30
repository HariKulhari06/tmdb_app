package com.hari.tmdb.shows.internal.items

import com.hari.tmdb.model.Show
import com.hari.tmdb.shows.R
import com.hari.tmdb.shows.databinding.ItemShowDetailBinding
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.xwray.groupie.databinding.BindableItem

class ItemShowDetail @AssistedInject constructor(
    @Assisted private val show: Show
) : BindableItem<ItemShowDetailBinding>() {

    override fun getLayout() = R.layout.item_show_detail

    override fun bind(viewBinding: ItemShowDetailBinding, position: Int) {
        viewBinding.show = show
    }


    @AssistedInject.Factory
    interface Factory {
        fun create(
            show: Show
        ): ItemShowDetail
    }
}