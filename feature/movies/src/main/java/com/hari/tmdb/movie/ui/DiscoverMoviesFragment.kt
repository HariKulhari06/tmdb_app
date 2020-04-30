package com.hari.tmdb.movie.ui

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.appyvet.materialrangebar.RangeBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.ChipGroup
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.hari.tmdb.ext.assistedActivityViewModels
import com.hari.tmdb.model.ExpandFilterState
import com.hari.tmdb.movie.R
import com.hari.tmdb.movie.databinding.DiscoverMoviesFragmentBinding
import com.hari.tmdb.movie.di.MovieAssistedInjectModule
import com.hari.tmdb.movie.viewmodel.DiscoverMoviesViewModel
import com.hari.tmdb.movie.viewmodel.MovieTabViewModel
import com.hari.tmdb.system.viewmodel.SystemViewModel
import com.hari.tmdb.ui.widget.BottomGestureSpace
import com.hari.tmdb.ui.widget.FilterChip
import com.hari.tmdb.ui.widget.onCheckedChanged
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject
import javax.inject.Provider

class DiscoverMoviesFragment : Fragment(R.layout.discover_movies_fragment), HasAndroidInjector {
    private lateinit var overrideBackPressedCallback: OnBackPressedCallback

    @Inject
    lateinit var discoverMoviesViewModelProvider: Provider<DiscoverMoviesViewModel>
    private val discoverMoviesViewModel: DiscoverMoviesViewModel by assistedActivityViewModels {
        discoverMoviesViewModelProvider.get()
    }

    @Inject
    lateinit var systemViewModelProvider: Provider<SystemViewModel>
    private val systemViewModel: SystemViewModel by assistedActivityViewModels {
        systemViewModelProvider.get()
    }

    @Inject
    lateinit var movieTabViewModelProvider: Provider<MovieTabViewModel>
    private val movieTabViewModel: MovieTabViewModel by assistedActivityViewModels({
        "Discover"
    }) {
        movieTabViewModelProvider.get()
    }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            setupMoviesFragment()
        }
        overrideBackPressedCallback =
            requireActivity().onBackPressedDispatcher.addCallback(this, false) {
                movieTabViewModel.setExpand(ExpandFilterState.EXPANDED)
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = DiscoverMoviesFragmentBinding.bind(view)
        val sessionSheetBehavior = BottomSheetBehavior.from(binding.sessionsSheet)
        initPageShapeAppearance(binding)

        val initialPeekHeight = sessionSheetBehavior.peekHeight
        val gestureSpace = BottomGestureSpace(resources)

        binding.sessionsSheet.doOnApplyWindowInsets { _, insets, _ ->
            sessionSheetBehavior.peekHeight =
                insets.systemWindowInsetBottom + initialPeekHeight + gestureSpace.gestureSpaceSize
            binding.filterView.updatePadding(
                bottom = initialPeekHeight + resources.getDimensionPixelSize(
                    R.dimen.movie_filter_view_padding_bottom
                )
            )
            // This block is the workaround to bottomSheetBehavior bug fix.
            // https://stackoverflow.com/questions/35685681/dynamically-change-height-of-bottomsheetbehavior
            if (sessionSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED)
                sessionSheetBehavior.onLayoutChild(
                    binding.fragmentSessionsCoordinator,
                    binding.sessionsSheet,
                    View.LAYOUT_DIRECTION_LTR
                )
        }
        binding.fragmentSessionsScrollView.doOnApplyWindowInsets { scrollView,
                                                                   insets,
                                                                   initialState ->
            // Set a bottom padding due to the system UI is enabled.
            scrollView.updatePadding(
                bottom = insets.systemWindowInsetBottom +
                        initialPeekHeight +
                        initialState.paddings.bottom
            )
        }
        sessionSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                movieTabViewModel.setExpand(
                    when (newState) {
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            ExpandFilterState.COLLAPSED
                        }
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            ExpandFilterState.EXPANDED
                        }
                        else -> {
                            ExpandFilterState.CHANGING
                        }
                    }
                )
            }
        })

        binding.seekBarRuntime.tickCount

        binding.seekBarRuntime.setOnRangeBarChangeListener(object :
            RangeBar.OnRangeBarChangeListener {
            override fun onTouchEnded(rangeBar: RangeBar?) {
            }

            override fun onRangeChangeListener(
                rangeBar: RangeBar?,
                leftPinIndex: Int,
                rightPinIndex: Int,
                leftPinValue: String?,
                rightPinValue: String?
            ) {

            }

            override fun onTouchStarted(rangeBar: RangeBar?) {

            }

        })

        binding.filterReset.setOnClickListener {
            discoverMoviesViewModel.resetFilter()
        }

        movieTabViewModel.uiModel.observe(viewLifecycleOwner) { uiModel ->
            when (uiModel.expandFilterState) {
                ExpandFilterState.EXPANDED -> {
                    sessionSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    overrideBackPressedCallback.isEnabled = false
                }
                ExpandFilterState.COLLAPSED -> {
                    sessionSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    overrideBackPressedCallback.isEnabled = true
                }
                ExpandFilterState.CHANGING -> Unit
            }
        }

        discoverMoviesViewModel.uiModel.observe(viewLifecycleOwner, Observer { uiModel ->
            uiModel.error?.let {
                systemViewModel.onError(it)
            }

            binding.genreFilters.setupFilter(
                allFilterSet = uiModel.allFilters.genres,
                currentFilterSet = uiModel.filters.genres,
                filterName = { it.name }
            ) { checked, genre ->
                discoverMoviesViewModel.filterChanged(genre, checked)
            }
            binding.adultFilters.setupFilter(
                allFilterSet = uiModel.allFilters.includeAdult,
                currentFilterSet = uiModel.filters.includeAdult,
                filterName = { it.toString().capitalize() }
            ) { checked, adult ->
                discoverMoviesViewModel.filterChanged(adult, checked)
            }

            binding.sortByFilters.setupFilter(
                allFilterSet = uiModel.allFilters.sortBy,
                currentFilterSet = uiModel.filters.sortBy,
                filterName = { it.name.sortByEnumNameToDisplayValue() }
            ) { checked, sortBy ->
                discoverMoviesViewModel.filterChanged(sortBy, checked)
            }

            binding.certificationFilters.setupFilter(
                allFilterSet = uiModel.allFilters.certifications,
                currentFilterSet = uiModel.filters.certifications,
                filterName = { it.name }
            ) { checked, certificate ->
                discoverMoviesViewModel.filterChanged(certificate, checked)
            }


            binding.languageFilters.setupFilter(
                allFilterSet = uiModel.allFilters.languages,
                currentFilterSet = uiModel.filters.languages,
                filterName = { it.englishName }
            ) { checked, language ->
                discoverMoviesViewModel.filterChanged(language, checked)
            }
        })


        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                // override back button iff front layer collapsed (filter is shown)
                overrideBackPressedCallback.isEnabled =
                    sessionSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED
            }
        })
    }

    private fun initPageShapeAppearance(binding: DiscoverMoviesFragmentBinding) {
        val shapeAppearanceModel =
            ShapeAppearanceModel.Builder()
                .setTopLeftCorner(
                    CornerFamily.ROUNDED,
                    resources.getDimension(R.dimen.bottom_sheet_corner_radius)
                )
                .build()
        /**
         * FrontLayer elevation is 1dp
         * https://material.io/components/backdrop/#anatomy
         */
        val materialShapeDrawable = MaterialShapeDrawable.createWithElevationOverlay(
            requireActivity(),
            resources.getDimension(R.dimen.bottom_sheet_elevation)
        ).apply {
            setShapeAppearanceModel(shapeAppearanceModel)
        }
        binding.sessionsSheet.background = materialShapeDrawable
    }

    private fun setupMoviesFragment() {

        val fragment = BottomSheetMoviesFragment.newInstance()

        childFragmentManager
            .beginTransaction()
            .replace(R.id.sessions_sheet, fragment, "Discover")
            .disallowAddToBackStack()
            .commit()
    }

    private inline fun <reified T> ChipGroup.setupFilter(
        allFilterSet: Set<T>,
        currentFilterSet: Set<T>,
        filterName: (T) -> String,
        crossinline onCheckChanged: (Boolean, T) -> Unit
    ) {
        // judge should we inflate chip?
        val shouldInflateChip = childCount == 0 || children.withIndex().any { (index, view) ->
            view.getTag(R.id.tag_filter) != allFilterSet.elementAtOrNull(index)
        }
        val filterToView = if (shouldInflateChip) {
            // filter data changed, so we should inflate it
            removeAllViews()
            allFilterSet.map { filter ->
                val chip =
                    layoutInflater.inflate(
                        R.layout.layout_chip,
                        this,
                        false
                    ) as FilterChip
                chip.onCheckedChangeListener = null
                chip.text = filterName(filter)
                chip.setTag(R.id.tag_filter, filter)
                addView(chip)
                filter to chip
            }.toMap()
        } else {
            // use existing view
            children.map { it.getTag(R.id.tag_filter) as T to it as FilterChip }.toMap()
        }

        // bind chip data
        filterToView.forEach { (filter, chip) ->
            val shouldChecked = currentFilterSet.contains(filter)
            if (chip.isChecked != shouldChecked) {
                chip.isChecked = shouldChecked
            }
            chip.onCheckedChanged { _, checked ->
                onCheckChanged(checked, filter)
            }
        }
    }
}


@Module
abstract class DiscoverMovieFragmentModule {
    @ContributesAndroidInjector(
        modules = [MovieAssistedInjectModule::class]
    )
    abstract fun contributeBottomSheetSessionsFragment(): BottomSheetMoviesFragment
}

