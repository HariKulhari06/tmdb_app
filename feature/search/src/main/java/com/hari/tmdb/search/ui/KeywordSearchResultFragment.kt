package com.hari.tmdb.search.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import com.hari.tmdb.di.PageScope
import com.hari.tmdb.ext.assistedActivityViewModels
import com.hari.tmdb.ext.assistedViewModels
import com.hari.tmdb.search.R
import com.hari.tmdb.search.databinding.FragmentKeywordResultBinding
import com.hari.tmdb.search.item.SearchItem
import com.hari.tmdb.search.viewmodel.KeywordSearchResultViewModel
import com.hari.tmdb.system.viewmodel.SystemViewModel
import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.databinding.GroupieViewHolder
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import jp.wasabeef.recyclerview.animators.LandingAnimator
import javax.inject.Inject
import javax.inject.Provider

class KeywordSearchResultFragment : Fragment(R.layout.fragment_keyword_result), HasAndroidInjector {

    @Inject
    lateinit var keywordSearchResultViewModelFactory: KeywordSearchResultViewModel.Factory
    private val keywordViewModel: KeywordSearchResultViewModel by assistedViewModels {
        keywordSearchResultViewModelFactory.create(navArgs.query)
    }

    @Inject
    lateinit var systemViewModelProvider: Provider<SystemViewModel>
    private val systemViewModel: SystemViewModel by assistedActivityViewModels {
        systemViewModelProvider.get()
    }

    @Inject
    lateinit var searchItemFactory: SearchItem.Factory

    private val navArgs: KeywordSearchResultFragmentArgs by navArgs()

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentKeywordResultBinding.bind(view)
        //binding.keywordRoot.transitionName = "${navArgs.query}-${TRANSITION_NAME_SUFFIX}"

        binding.lifecycleOwner = viewLifecycleOwner
        binding.keywordViewModel = keywordViewModel

        val adapter = GroupAdapter<GroupieViewHolder<*>>()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.itemAnimator = LandingAnimator()

        keywordViewModel.ui.observe(viewLifecycleOwner, Observer { uiModel ->
            uiModel.error?.let { error ->
                systemViewModel.onError(error)
            }

            uiModel.movies?.let { movies ->
                val items = mutableListOf<Group>()
                items += movies.map { movie -> searchItemFactory.create(movie = movie) }
                adapter.update(items)
            }

        })
    }

}

@Module
abstract class KeywordFragmentModule {

    companion object {
        @PageScope
        @Provides
        fun providesLifecycleOwnerLiveData(
            keywordFragment: KeywordSearchResultFragment
        ): LiveData<LifecycleOwner> {
            return keywordFragment.viewLifecycleOwnerLiveData
        }
    }
}
