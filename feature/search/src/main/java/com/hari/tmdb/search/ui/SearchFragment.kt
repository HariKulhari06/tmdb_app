package com.hari.tmdb.search.ui

import android.app.Activity
import android.app.SearchManager
import android.os.Bundle
import android.transition.TransitionManager
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SearchView
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.hari.tmdb.di.PageScope
import com.hari.tmdb.ext.assistedActivityViewModels
import com.hari.tmdb.ext.assistedViewModels
import com.hari.tmdb.search.R
import com.hari.tmdb.search.databinding.FragmentSearchBinding
import com.hari.tmdb.search.item.HeaderItem
import com.hari.tmdb.search.item.KeywordItem
import com.hari.tmdb.search.item.SearchItem
import com.hari.tmdb.search.viewmodel.SearchViewModel
import com.hari.tmdb.system.viewmodel.SystemViewModel
import com.hari.tmdb.util.AppcompatRId
import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.databinding.GroupieViewHolder
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import javax.inject.Inject
import javax.inject.Provider

class SearchFragment : Fragment(R.layout.fragment_search), HasAndroidInjector {

    @Inject
    lateinit var searchViewModelProvider: Provider<SearchViewModel>
    private val searchViewModel: SearchViewModel by assistedViewModels {
        searchViewModelProvider.get()
    }

    @Inject
    lateinit var systemViewModelProvider: Provider<SystemViewModel>
    private val systemViewModel: SystemViewModel by assistedActivityViewModels {
        systemViewModelProvider.get()
    }

    @Inject
    lateinit var searchItemFactory: SearchItem.Factory

    @Inject
    lateinit var headerItemFactory: HeaderItem.Factory

    @Inject
    lateinit var keywordItemFactory: KeywordItem.Factory

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onDestroyView() {
        super.onDestroyView()
        view?.let {
            val imm =
                context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val forward = MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.Z, true)
        enterTransition = forward

        val backward = MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.Z, false)
        returnTransition = backward
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSearchBinding.bind(view)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = searchViewModel

        val adapter = GroupAdapter<GroupieViewHolder<*>>()
        binding.recyclerViewSearch.adapter = adapter
        binding.recyclerViewSearch.itemAnimator = SlideInUpAnimator()

        searchViewModel.ui.observe(viewLifecycleOwner, Observer { uiModel ->
            uiModel.error?.let {
                systemViewModel.onError(it)
            }

            uiModel.movies?.let { movies ->
                setRecentSearchUiVisibility(binding, movies.isEmpty())
                setNoResultStateVisibility(
                    binding,
                    (movies.isEmpty() && searchViewModel.getSearchQuery().isNullOrEmpty().not())
                )
                val items = mutableListOf<Group>()
                items += movies.map { movie ->
                    searchItemFactory.create(movie)
                }

                adapter.update(items)

                /* uiModel.keywords?.let { keywords ->
                     items.add(headerItemFactory.create(getString(R.string.explore_keywords_related_to)))
                     items += keywords.map { keyword ->
                         keywordItemFactory.create(keyword)
                     }
                     adapter.addAll(items)
                 }*/
            }


        })
    }

    private fun setRecentSearchUiVisibility(binding: FragmentSearchBinding, isVisible: Boolean) {
        val fadeThrough = MaterialFadeThrough.create(requireContext())
        TransitionManager.beginDelayedTransition(binding.container, fadeThrough)

        if (isVisible) {
            binding.recentSearchViewGroup.visibility = View.VISIBLE
            binding.textMoviesTvShowsPerson.visibility = View.INVISIBLE
        } else {
            binding.recentSearchViewGroup.visibility = View.GONE
            binding.textMoviesTvShowsPerson.visibility = View.VISIBLE
        }

    }

    private fun setNoResultStateVisibility(binding: FragmentSearchBinding, isVisible: Boolean) {
        val fadeThrough = MaterialFadeThrough.create(requireContext())
        TransitionManager.beginDelayedTransition(binding.container, fadeThrough)
        if (isVisible) {
            binding.noResultState.visibility = View.VISIBLE
        } else {
            binding.noResultState.visibility = View.GONE
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)
        val searchView = menu.findItem(R.id.search_view).actionView as SearchView
        val context = requireContext()
        context.getSystemService<SearchManager>()?.let { searchManager ->
            searchView.setSearchableInfo(
                searchManager.getSearchableInfo(requireActivity().componentName)
            )
        }
        (searchView.findViewById(AppcompatRId.search_button) as ImageView).setColorFilter(
            AppCompatResources.getColorStateList(
                context,
                R.color.search_icon
            ).defaultColor
        )
        (searchView.findViewById(AppcompatRId.search_close_btn) as ImageView).setColorFilter(
            AppCompatResources.getColorStateList(
                context,
                R.color.search_close_icon
            ).defaultColor
        )
        val searchVoiceButton = searchView.findViewById(AppcompatRId.search_voice_btn) as ImageView
        searchVoiceButton.setImageResource(R.drawable.ic_keyboard_voice_24px)
        searchVoiceButton.setColorFilter(
            AppCompatResources.getColorStateList(
                context,
                R.color.search_voice_icon
            ).defaultColor
        )

        val searchText = searchView.findViewById(AppcompatRId.search_src_text) as EditText
        searchText.setHintTextColor(
            AppCompatResources.getColorStateList(
                context,
                R.color.search_hint
            ).defaultColor
        )
        searchText.setTextColor(
            AppCompatResources.getColorStateList(
                context,
                R.color.search_voice_icon
            ).defaultColor
        )
        searchView.isIconified = false
        searchView.clearFocus()
        searchView.queryHint = resources.getString(R.string.query_hint)
        searchViewModel.query.value?.let { query ->
            searchView.setQuery(query, false)
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                searchViewModel.updateSearchQuery(s)
                return false
            }
        })
        searchView.setOnCloseListener {
            searchView.setQuery("", true)
            true
        }
        searchView.maxWidth = Int.MAX_VALUE
    }

}

@Module
abstract class SearchFragmentModule {
    companion object {
        @PageScope
        @Provides
        fun providesLifecycleOwnerLiveData(
            searchFragment: SearchFragment
        ): LiveData<LifecycleOwner> {
            return searchFragment.viewLifecycleOwnerLiveData
        }
    }
}