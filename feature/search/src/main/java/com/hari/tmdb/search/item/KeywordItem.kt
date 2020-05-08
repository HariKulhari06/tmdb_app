package com.hari.tmdb.search.item

import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.hari.tmdb.model.Keyword
import com.hari.tmdb.search.R
import com.hari.tmdb.search.databinding.ItemKeywordBinding
import com.hari.tmdb.search.ui.SearchFragmentDirections.Companion.actionSearchToKeywordResult
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.xwray.groupie.databinding.BindableItem

class KeywordItem @AssistedInject constructor(
    @Assisted private val keyword: Keyword
) : BindableItem<ItemKeywordBinding>(keyword.id.hashCode().toLong()) {
    companion object {
        private const val TRANSITION_NAME_SUFFIX = "keyword"
    }

    override fun getLayout(): Int = R.layout.item_keyword

    override fun bind(viewBinding: ItemKeywordBinding, position: Int) {
        viewBinding.keyword = keyword
        viewBinding.keywordRoot.setOnClickListener {
            val extra = FragmentNavigatorExtras(
                viewBinding.keywordRoot to viewBinding.keywordRoot.transitionName
            )
            viewBinding.root.findNavController().navigate(
                actionSearchToKeywordResult(keyword.name),
                extra
            )
            //  viewBinding.root.transitionName = "${keyword.name}-$TRANSITION_NAME_SUFFIX"
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            keyword: Keyword
        ): KeywordItem
    }
}