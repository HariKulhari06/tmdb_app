package com.hari.tmdb.account.item


import com.hari.tmdb.account.R
import com.hari.tmdb.account.databinding.ItemAccountDetailBinding
import com.xwray.groupie.databinding.BindableItem

class AccountDetailsListItem :
    BindableItem<ItemAccountDetailBinding>() {
    override fun getLayout(): Int = R.layout.item_account_detail
    override fun bind(viewBinding: ItemAccountDetailBinding, position: Int) {

    }
}