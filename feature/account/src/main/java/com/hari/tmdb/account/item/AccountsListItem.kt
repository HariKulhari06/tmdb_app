package com.hari.tmdb.account.item


import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.hari.tmdb.account.R
import com.hari.tmdb.account.databinding.ItemAccountListBinding
import com.hari.tmdb.account.ui.AccountFragmentDirections.Companion.actionAccountToSettings
import com.hari.tmdb.model.AccountsItem
import com.xwray.groupie.databinding.BindableItem

class AccountsListItem(private val item: AccountsItem) : BindableItem<ItemAccountListBinding>() {
    override fun getLayout(): Int = R.layout.item_account_list
    override fun bind(viewBinding: ItemAccountListBinding, position: Int) {
        with(viewBinding) {
            icon.setImageDrawable(item.icon)
            title.text = item.title
            iconArrow.isVisible = true
            root.setOnClickListener {
                if (item.title == title.context.getString(R.string.settings)) {
                    root.findNavController().navigate(actionAccountToSettings())
                }

            }

            if (item.title == title.context.getString(R.string.logout)) {
                title.setTextColor(
                    AppCompatResources.getColorStateList(
                        icon.context,
                        R.color.design_default_color_error

                    ).defaultColor
                )
                icon.setColorFilter(
                    AppCompatResources.getColorStateList(
                        icon.context,
                        R.color.design_default_color_error

                    ).defaultColor
                )
            }
        }
    }
}