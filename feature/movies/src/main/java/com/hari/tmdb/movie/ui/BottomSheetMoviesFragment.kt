package com.hari.tmdb.movie.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.hari.tmdb.di.PageScope
import com.hari.tmdb.ext.assistedActivityViewModels
import com.hari.tmdb.ext.awaitNextLayout
import com.hari.tmdb.model.ExpandFilterState.COLLAPSED
import com.hari.tmdb.movie.R
import com.hari.tmdb.movie.databinding.BottomSheetMoviesFragmentBinding
import com.hari.tmdb.movie.item.MovieItem
import com.hari.tmdb.movie.viewmodel.DiscoverMoviesViewModel
import com.hari.tmdb.movie.viewmodel.MovieTabViewModel
import com.hari.tmdb.ui.widget.BottomGestureSpace
import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.databinding.GroupieViewHolder
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import javax.inject.Inject
import javax.inject.Provider

class BottomSheetMoviesFragment : Fragment(R.layout.bottom_sheet_movies_fragment),
    HasAndroidInjector {

    @Inject
    lateinit var sessionTabViewModelProvider: Provider<MovieTabViewModel>
    private val sessionTabViewModel: MovieTabViewModel by assistedActivityViewModels({
        "Discover"
    }) {
        sessionTabViewModelProvider.get()
    }

    @Inject
    lateinit var discoverMoviesViewModelProvider: Provider<DiscoverMoviesViewModel>
    private val discoverMoviesViewModel: DiscoverMoviesViewModel by assistedActivityViewModels {
        discoverMoviesViewModelProvider.get()
    }

    @Inject
    lateinit var movieItemFactory: MovieItem.Factory

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = BottomSheetMoviesFragmentBinding.bind(view)

        binding.isEmptyFavoritePage = false
        val groupAdapter = GroupAdapter<GroupieViewHolder<*>>()
        binding.movieRecycler.adapter = groupAdapter

        binding.movieRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val isVisibleShadow =
                    binding.movieRecycler.canScrollVertically(-1)

                binding.divider.isVisible = !isVisibleShadow
                binding.dividerShadow.isVisible = isVisibleShadow
            }
        })
        binding.startFilter.setOnClickListener {
            sessionTabViewModel.toggleExpand()
        }
        binding.expandLess.setOnClickListener {
            sessionTabViewModel.toggleExpand()
        }
        binding.movieRecycler.doOnApplyWindowInsets { sessionRecycler, insets, initialState ->
            sessionRecycler.updatePadding(
                bottom = insets.systemWindowInsetBottom + initialState.paddings.bottom
            )
        }
        val gestureSpace = BottomGestureSpace(resources)
        val peekHeight =
            resources.getDimensionPixelSize(R.dimen.bottom_sheet_default_peek_height)

        binding.sessionMotionLayout.doOnApplyWindowInsets { _, insets, _ ->
            lifecycleScope.launchWhenStarted {
                binding.startFilter.awaitNextLayout()
                val filterButtonHeight = binding.startFilter.measuredHeight
                binding.sessionMotionLayout
                    .getConstraintSet(R.id.collapsed)?.let { constraintSet ->
                        val bottomSpace = peekHeight - filterButtonHeight
                        val y = gestureSpace.gestureSpaceSize +
                                insets.systemWindowInsetBottom.toFloat() +
                                bottomSpace
                        constraintSet.setTranslationY(
                            R.id.divider,
                            y
                        )
                        constraintSet.setTranslationY(
                            R.id.divider_shadow,
                            y
                        )
                        constraintSet.setTranslationY(
                            R.id.session_recycler,
                            y
                        )
                    }
            }
        }

        sessionTabViewModel.uiModel.observe(viewLifecycleOwner) { uiModel ->
            val shouldBeCollapsed = when (uiModel.expandFilterState) {
                COLLAPSED ->
                    true
                else ->
                    false
            }
            if (binding.isCollapsed != shouldBeCollapsed) {
                TransitionManager.beginDelayedTransition(
                    binding.movieRecycler.parent as ViewGroup
                )
                binding.isCollapsed = shouldBeCollapsed
            }
        }

        discoverMoviesViewModel.uiModel.observe(viewLifecycleOwner, Observer { uiModel ->
            uiModel.movies?.let { movies ->
                val items = mutableListOf<Group>()
                items += movies.map {
                    movieItemFactory.create(it)
                }
                groupAdapter.update(items)
            }
        })
    }


    companion object {
        fun newInstance(): BottomSheetMoviesFragment {
            return BottomSheetMoviesFragment()
        }
    }
}

@Module
abstract class MoviesFragmentModule {

    companion object {
        @PageScope
        @Provides
        fun providesLifecycleOwnerLiveData(
            bottomSheetMoviesFragment: BottomSheetMoviesFragment
        ): LiveData<LifecycleOwner> {
            return bottomSheetMoviesFragment.viewLifecycleOwnerLiveData
        }
    }
}
