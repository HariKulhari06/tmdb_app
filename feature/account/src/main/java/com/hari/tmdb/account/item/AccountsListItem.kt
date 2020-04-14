package com.hari.tmdb.account.item

import androidx.core.view.isVisible
import coil.Coil
import coil.api.load
import coil.transform.RoundedCornersTransformation
import com.hari.tmdb.account.R
import com.hari.tmdb.account.databinding.ItemAccountListBinding
import com.hari.tmdb.model.AccountsItem
import com.xwray.groupie.databinding.BindableItem

class AccountsListItem(private val item: AccountsItem) : BindableItem<ItemAccountListBinding>() {
    override fun getLayout(): Int = R.layout.item_account_list
    override fun bind(viewBinding: ItemAccountListBinding, position: Int) {
        with(viewBinding) {
            Coil
                .load(icon.context, item.icon) {
                    crossfade(true)
                    transformations(RoundedCornersTransformation(15.0f))
                    target(icon)
                }

            title.text = item.title
            iconArrow.isVisible = item.showArrow
            root.setOnClickListener {

            }
        }

    }


}