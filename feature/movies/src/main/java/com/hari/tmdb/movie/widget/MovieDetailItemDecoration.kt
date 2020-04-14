package com.hari.tmdb.movie.widget

import android.content.Context
import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import com.hari.tmdb.movie.item.MovieDetailRelated
import com.xwray.groupie.GroupAdapter

/**
 * For setting top and bottom margin of speaker
 */
class MovieDetailItemDecoration(
    private val adapter: GroupAdapter<*>,
    private val context: Context
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        val item = adapter.getItem(itemPosition)
        val dp = context.resources.displayMetrics.density
        var topMargin = 0 * dp
        var bottomMargin = 0 * dp
        if (item is MovieDetailRelated) {
            if (item.first) {
                // First speaker
                topMargin = 18 * dp
            } else {
                // Other speaker
                topMargin = 8 * dp
            }
        }
        if (itemPosition == adapter.itemCount - 1) {
            // Last item
            bottomMargin += 18 * dp
            if (item is MovieDetailRelated) {
                // Last item is speaker
                bottomMargin += 32 * dp
            }
        }
        outRect.set(0, topMargin.toInt(), 0, bottomMargin.toInt())
    }
}
