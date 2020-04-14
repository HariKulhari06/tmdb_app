package com.hari.tmdb.movie.item

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.isGone
import com.hari.tmdb.movie.R
import com.hari.tmdb.movie.databinding.ItemHeaderBinding
import com.xwray.groupie.databinding.BindableItem

class HeaderItem constructor(
    @StringRes val titleStringResId: Int,
    @StringRes val subtitleResId: Int = 0,
    @DrawableRes val iconResId: Int = 0,
    private val onIconClickListener: () -> Unit
) : BindableItem<ItemHeaderBinding>() {


    override fun getLayout(): Int {
        return R.layout.item_header
    }

    override fun bind(
        viewBinding: ItemHeaderBinding,
        position: Int
    ) {

        with(viewBinding) {

            title.setText(titleStringResId)

            if (subtitleResId > 0) {
                subtitle.setText(subtitleResId)
            } else {
                subtitle.isGone = true
            }

            if (iconResId > 0) {
                icon.setImageResource(iconResId)
                icon.setOnClickListener {
                    onIconClickListener.invoke()
                }
            } else {
                icon.isGone = true
            }

        }

    }

}