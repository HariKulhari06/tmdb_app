package com.hari.tmdb.shows.internal.items

import com.hari.tmdb.model.Show
import com.hari.tmdb.shows.R
import com.hari.tmdb.shows.databinding.ItemTopTrandingBannerBinding
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.xwray.groupie.databinding.BindableItem

class ItemTopTrendingBanner @AssistedInject constructor(
    @Assisted private val show: Show
) : BindableItem<ItemTopTrandingBannerBinding>() {

    override fun getLayout() = R.layout.item_top_tranding_banner

    override fun bind(viewBinding: ItemTopTrandingBannerBinding, position: Int) {
        viewBinding.show = show
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            show: Show
        ): ItemTopTrendingBanner
    }
}