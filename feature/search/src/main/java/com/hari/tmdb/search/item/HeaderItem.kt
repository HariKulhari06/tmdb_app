package com.hari.tmdb.search.item

import com.hari.tmdb.search.R
import com.hari.tmdb.search.databinding.ItemSearchHeaderBinding
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.xwray.groupie.databinding.BindableItem

class HeaderItem @AssistedInject constructor(
    @Assisted private val title: String
) : BindableItem<ItemSearchHeaderBinding>(title.hashCode().toLong()) {

    override fun getLayout(): Int = R.layout.item_search_header

    override fun bind(viewBinding: ItemSearchHeaderBinding, position: Int) {
        with(viewBinding) {
            titleText.text = title
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            title: String
        ): HeaderItem
    }
}