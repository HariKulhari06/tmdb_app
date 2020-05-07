package com.hari.tmdb.search.item

import com.hari.tmdb.model.Keyword
import com.hari.tmdb.search.R
import com.hari.tmdb.search.databinding.ItemKeywordBinding
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.xwray.groupie.databinding.BindableItem

class KeywordItem @AssistedInject constructor(
    @Assisted private val keyword: Keyword
) : BindableItem<ItemKeywordBinding>(keyword.id.hashCode().toLong()) {

    override fun getLayout(): Int = R.layout.item_keyword

    override fun bind(viewBinding: ItemKeywordBinding, position: Int) {
        viewBinding.keyword = keyword
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            keyword: Keyword
        ): KeywordItem
    }
}