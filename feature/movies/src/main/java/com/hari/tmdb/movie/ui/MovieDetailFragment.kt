package com.hari.tmdb.movie.ui

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.hari.tmdb.di.Injectable
import com.hari.tmdb.di.PageScope
import com.hari.tmdb.ext.assistedActivityViewModels
import com.hari.tmdb.ext.assistedViewModels
import com.hari.tmdb.groupie.CarouselItemDecoration
import com.hari.tmdb.movie.R
import com.hari.tmdb.movie.databinding.MovieDetailFragmentBinding
import com.hari.tmdb.movie.item.*
import com.hari.tmdb.movie.viewmodel.MovieDetailViewModel
import com.hari.tmdb.movie.widget.MovieDetailItemDecoration
import com.hari.tmdb.system.viewmodel.SystemViewModel
import com.hari.tmdb.ui.item.CarouselGroup
import com.hari.tmdb.ui.item.HeaderItem
import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.databinding.GroupieViewHolder
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Provider

class MovieDetailFragment : Fragment(R.layout.movie_detail_fragment), Injectable {

    private var mBundleRecyclerViewState: Bundle? = null

    @Inject
    lateinit var systemViewModelFactory: Provider<SystemViewModel>
    private val systemViewModel by assistedActivityViewModels {
        systemViewModelFactory.get()
    }

    @Inject
    lateinit var movieDetailViewModelFactory: MovieDetailViewModel.Factory
    private val movieDetailViewModel by assistedViewModels {
        movieDetailViewModelFactory.create(navArgs.movieId)
    }
    private val navArgs: MovieDetailFragmentArgs by navArgs()

    @Inject
    lateinit var movieDetailTitleItemFactory: MovieDetailTitleItem.Factory

    @Inject
    lateinit var movieDetailAboutItemFactory: MovieDetailAboutItem.Factory

    @Inject
    lateinit var movieDetailVideosItemFactory: MovieDetailVideoItem.Factory

    @Inject
    lateinit var movieDetailRelatedItemFactory: MovieDetailRelated.Factory

    @Inject
    lateinit var movieDetailCastingFactory: MovieDetailCasting.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = buildContainerTransform()
        sharedElementReturnTransition = buildContainerTransform()
    }

    private fun buildContainerTransform() =
        MaterialContainerTransform(requireContext()).apply {
            drawingViewId = R.id.coordinator
            interpolator = FastOutSlowInInterpolator()
            pathMotion = MaterialArcMotion()
            fadeMode = MaterialContainerTransform.FADE_MODE_OUT
            duration = 300
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = MovieDetailFragmentBinding.bind(view)
        binding.coordinator.transitionName = navArgs.movieId.toString()

        // postponeEnterTransition()
        val adapter = GroupAdapter<GroupieViewHolder<*>>()
        binding.movieDetailRecycler.adapter = adapter

        context?.let {
            binding.movieDetailRecycler.addItemDecoration(
                MovieDetailItemDecoration(
                    adapter,
                    it
                )
            )
        }

        val carouselDecoration = CarouselItemDecoration(
            ContextCompat.getColor(
                requireContext(),
                R.color.color_surface
            ), resources.getDimensionPixelSize(R.dimen.space_carousel)
        )

        movieDetailViewModel.ui.observe(viewLifecycleOwner, Observer { uiModel ->
            uiModel.error?.let {
                systemViewModel.onError(it)
            }

            uiModel.movie?.let { movie ->
                binding.movieDetailRecycler.transitionName = "movie"
                val items = mutableListOf<Group>()
                items += movieDetailTitleItemFactory.create(movie)
                items += movieDetailAboutItemFactory.create(movie)

                adapter.update(items)

                val castingSection =
                    Section(HeaderItem(titleStringResId = R.string.top_paid_casting) {})
                castingSection.setHideWhenEmpty(true)

                val castingAdapter = GroupAdapter<GroupieViewHolder<*>>()
                castingAdapter.addAll(
                    movie.cast.sortedBy { it.order }.map { cast ->
                        movieDetailCastingFactory.create(cast)
                    }
                )

                castingSection.add(
                    CarouselGroup(
                        itemDecoration = carouselDecoration,
                        adapter = castingAdapter,
                        layoutManager = linearLayoutManager()
                    )
                )
                adapter.add(castingSection)


                val videoSection = Section(
                    HeaderItem(
                        titleStringResId = R.string.videos
                    ) {})
                videoSection.setHideWhenEmpty(true)

                val videoAdapter = GroupAdapter<GroupieViewHolder<*>>()
                videoAdapter.addAll(
                    movie.videos.map { video ->
                        movieDetailVideosItemFactory.create(video)
                    }
                )

                videoSection.add(
                    CarouselGroup(
                        itemDecoration = carouselDecoration,
                        adapter = videoAdapter,
                        layoutManager = linearLayoutManager()
                    )
                )
                adapter.add(videoSection)
            }

            //  startPostponedEnterTransition()
        })
    }


    private fun linearLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
    }

}

@Module
abstract class MovieDetailFragmentModule {
    companion object {
        @PageScope
        @Provides
        fun providesLifecycleOwnerLiveData(
            movieDetailFragment: MovieDetailFragment
        ): LiveData<LifecycleOwner> {
            return movieDetailFragment.viewLifecycleOwnerLiveData
        }
    }
}