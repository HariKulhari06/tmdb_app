package com.hari.tmdb.groupie

import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView

class CarouselItemDecoration(
    @ColorInt val backgroundColor: Int,
    private val paddingPixelSize: Int,
    private val firstAndLastItemPadding: Int = 0

) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val isLast = position == state.itemCount - 1

        if (position == 0) {
            outRect.left = firstAndLastItemPadding
            outRect.right = paddingPixelSize
        } else {
            outRect.right = paddingPixelSize
        }

        if (isLast) {
            outRect.right = firstAndLastItemPadding
        }
    }
}