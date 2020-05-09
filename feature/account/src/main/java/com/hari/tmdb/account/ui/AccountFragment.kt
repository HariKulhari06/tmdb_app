package com.hari.tmdb.account.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.hari.tmdb.account.R
import com.hari.tmdb.account.databinding.AccountFragmentBinding
import com.hari.tmdb.account.item.AccountDetailsListItem
import com.hari.tmdb.account.item.AccountsListItem
import com.hari.tmdb.account.viewmodel.AccountsViewModel
import com.hari.tmdb.ext.assistedActivityViewModels
import com.hari.tmdb.ext.assistedViewModels
import com.hari.tmdb.model.AccountsItem
import com.hari.tmdb.system.viewmodel.SystemViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.databinding.GroupieViewHolder
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject
import javax.inject.Provider

class AccountFragment : Fragment(R.layout.account_fragment), HasAndroidInjector {

    @Inject
    lateinit var accountViewModelProvider: Provider<AccountsViewModel>
    private val searchViewModel: AccountsViewModel by assistedViewModels {
        accountViewModelProvider.get()
    }

    @Inject
    lateinit var systemViewModelProvider: Provider<SystemViewModel>
    private val systemViewModel: SystemViewModel by assistedActivityViewModels {
        systemViewModelProvider.get()
    }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = AccountFragmentBinding.bind(view)
        val adapter = GroupAdapter<GroupieViewHolder<*>>()
        binding.accountsItemRecycler.adapter = adapter
        adapter.add(AccountDetailsListItem())


        val accountItem = resources.getStringArray(R.array.account_list_item)
        val icons = resources.obtainTypedArray(R.array.account_options_icon)
        accountItem.forEachIndexed { index, s ->
            adapter.add(AccountsListItem(AccountsItem(icon = icons.getDrawable(index), title = s)))

        }
    }
}