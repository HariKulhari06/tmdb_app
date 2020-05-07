package com.hari.tmdb.ui.item

import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupDataObserver
import com.xwray.groupie.Item

/**
 * A group that contains a single carousel item and is empty when the carousel is empty
 */
class CarouselGroup(
    itemDecoration: RecyclerView.ItemDecoration? = null,
    adapter: GroupAdapter<*>,
    layoutManager: RecyclerView.LayoutManager?
) : Group {
    private var isEmpty: Boolean
    private var groupDataObserver: GroupDataObserver? = null
    private val carouselItem: CarouselItem = CarouselItem(itemDecoration, adapter, layoutManager!!)

    override fun getItemCount(): Int {
        return if (isEmpty) 0 else 1
    }

    override fun getItem(position: Int): Item<*> {
        return if (position == 0 && !isEmpty) carouselItem else throw IndexOutOfBoundsException()
    }

    override fun getPosition(item: Item<*>): Int {
        return if (item === carouselItem && !isEmpty) 0 else -1
    }

    override fun registerGroupDataObserver(groupDataObserver: GroupDataObserver) {
        this.groupDataObserver = groupDataObserver
    }

    override fun unregisterGroupDataObserver(groupDataObserver: GroupDataObserver) {
        this.groupDataObserver = null
    }

    init {
        isEmpty = adapter.itemCount == 0
        val adapterDataObserver: RecyclerView.AdapterDataObserver =
            object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeRemoved(
                    positionStart: Int,
                    itemCount: Int
                ) {
                    val empty = adapter.itemCount == 0
                    if (empty && !isEmpty) {
                        isEmpty = true
                        groupDataObserver!!.onItemRemoved(carouselItem, 0)
                    }
                }

                override fun onItemRangeInserted(
                    positionStart: Int,
                    itemCount: Int
                ) {
                    val empty = adapter.itemCount == 0
                    if (isEmpty && !empty) {
                        isEmpty = false
                        groupDataObserver!!.onItemInserted(carouselItem, 0)
                    }
                }
            }
        adapter.registerAdapterDataObserver(adapterDataObserver)
    }
}