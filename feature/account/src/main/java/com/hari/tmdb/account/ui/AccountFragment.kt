package com.hari.tmdb.account.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.Coil
import coil.api.load
import coil.transform.RoundedCornersTransformation
import com.hari.tmdb.account.R
import com.hari.tmdb.account.databinding.AccountFragmentBinding
import com.hari.tmdb.account.item.AccountsListItem
import com.hari.tmdb.model.AccountsItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.databinding.GroupieViewHolder

class AccountFragment : Fragment(R.layout.account_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = AccountFragmentBinding.bind(view)
        val adapter = GroupAdapter<GroupieViewHolder<*>>()
        binding.accountsItemRecycler.adapter = adapter

        binding.backArrow.setOnClickListener { findNavController().popBackStack() }

        val accountItem = resources.getStringArray(R.array.account_list_item)
        accountItem.forEach {
            adapter.add(AccountsListItem(AccountsItem(icon = R.drawable.poster, title = it)))
        }

        Coil
            .load(requireContext(), R.drawable.poster) {
                crossfade(true)
                transformations(RoundedCornersTransformation(15.0f))
                target(binding.avatarImageView)
            }

    }
}