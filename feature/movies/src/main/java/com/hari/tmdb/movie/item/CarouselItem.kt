package com.hari.tmdb.movie.item

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hari.tmdb.movie.R
import com.hari.tmdb.movie.databinding.ItemCarouselBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.databinding.GroupieViewHolder

/**
 * A horizontally scrolling RecyclerView, for use in a vertically scrolling RecyclerView.
 */
class CarouselItem internal constructor(
    private val carouselDecoration: RecyclerView.ItemDecoration? = null,
    private val adapter: GroupAdapter<*>,
    private val layoutManager: RecyclerView.LayoutManager
) : BindableItem<ItemCarouselBinding>() {
    override fun createViewHolder(itemView: View): GroupieViewHolder<ItemCarouselBinding> {
        val viewHolder = super.createViewHolder(itemView)
        val recyclerView = viewHolder.binding.recyclerView

        carouselDecoration?.let { recyclerView.addItemDecoration(it) }

        recyclerView.layoutManager = layoutManager

        return viewHolder
    }

    override fun bind(
        viewBinding: ItemCarouselBinding,
        position: Int
    ) {
        viewBinding.recyclerView.adapter = adapter
    }

    override fun getLayout(): Int {
        return R.layout.item_carousel
    }

}