package com.hari.tmdb.movie.ui

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.material.transition.Hold
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
import com.hari.tmdb.ui.animation.MEDIUM_EXPAND_DURATION
import com.hari.tmdb.ui.transaction.fadeThrough
import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.databinding.GroupieViewHolder
import dagger.Module
import dagger.Provides
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import javax.inject.Inject
import javax.inject.Provider

class MovieDetailFragment : Fragment(R.layout.movie_detail_fragment), Injectable {

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
    lateinit var movieDetailRelatedItemFactory: MovieDetailRelated.Factory

    @Inject
    lateinit var movieDetailCastingFactory: MovieDetailCasting.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = fadeThrough().apply {
            duration = MEDIUM_EXPAND_DURATION
        }
        exitTransition = Hold()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // postponeEnterTransition()
        val binding = MovieDetailFragmentBinding.bind(view)
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

        val itemAnimator = binding.movieDetailRecycler.itemAnimator
        if (itemAnimator is SimpleItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }

        binding.movieDetailRecycler.itemAnimator = SlideInUpAnimator()

        binding.movieDetailRecycler.transitionName = "movie"


        val carouselDecoration = CarouselItemDecoration(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
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
            }

        })


        val castingSection = Section(HeaderItem(titleStringResId = R.string.top_paid_casting) {})
        castingSection.setHideWhenEmpty(true)
        castingSection.add(castingGroup(carouselDecoration))
        adapter.add(castingSection)

        val relatedMovieSection = Section(HeaderItem(titleStringResId = R.string.releted_movies) {})
        relatedMovieSection.setHideWhenEmpty(true)
        relatedMovieSection.add(relatedMoviesGroup(carouselDecoration))
        adapter.add(relatedMovieSection)

        //  startPostponedEnterTransition()
    }

    private fun castingGroup(carouselDecoration: CarouselItemDecoration): CarouselGroup {
        val relatedMoviesAdapter = GroupAdapter<GroupieViewHolder<*>>()

        return CarouselGroup(carouselDecoration, relatedMoviesAdapter)
    }


    private fun relatedMoviesGroup(carouselDecoration: CarouselItemDecoration): CarouselGroup {
        val relatedMoviesAdapter = GroupAdapter<GroupieViewHolder<*>>()

        return CarouselGroup(carouselDecoration, relatedMoviesAdapter)
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