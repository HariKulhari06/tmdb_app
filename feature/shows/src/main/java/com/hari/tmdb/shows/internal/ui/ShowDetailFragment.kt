package com.hari.tmdb.shows.internal.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.hari.tmdb.di.Injectable
import com.hari.tmdb.di.PageScope
import com.hari.tmdb.ext.assistedActivityViewModels
import com.hari.tmdb.ext.assistedViewModels
import com.hari.tmdb.model.Show
import com.hari.tmdb.shows.R
import com.hari.tmdb.shows.databinding.ShowDetailFragmentBinding
import com.hari.tmdb.shows.internal.items.ItemShowDetail
import com.hari.tmdb.shows.internal.viewmodel.ShowDetailViewModel
import com.hari.tmdb.system.viewmodel.SystemViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.databinding.GroupieViewHolder
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Provider

class ShowDetailFragment : Fragment(R.layout.show_detail_fragment), Injectable {

    @Inject
    lateinit var showDetailViewModelFactory: ShowDetailViewModel.Factory
    private val showDetailViewModel: ShowDetailViewModel by assistedViewModels {
        showDetailViewModelFactory.create(navArgs.id)
    }

    @Inject
    lateinit var systemViewModelProvider: Provider<SystemViewModel>
    private val systemViewModel: SystemViewModel by assistedActivityViewModels {
        systemViewModelProvider.get()
    }

    private val navArgs: ShowDetailFragmentArgs by navArgs()

    @Inject
    lateinit var itemShowDetailFactory: ItemShowDetail.Factory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = buildContainerTransform()
        sharedElementReturnTransition = buildContainerTransform()
    }

    private fun buildContainerTransform() =
        MaterialContainerTransform(requireContext()).apply {
            drawingViewId = R.id.shows_detail_root
            interpolator = FastOutSlowInInterpolator()
            pathMotion = MaterialArcMotion()
            fadeMode = MaterialContainerTransform.FADE_MODE_OUT
            duration = 300
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = ShowDetailFragmentBinding.bind(view)
        binding.showsDetailRoot.transitionName = navArgs.id.toString()
        val adapter = GroupAdapter<GroupieViewHolder<*>>()

        binding.recyclerViewShowDetail.adapter = adapter

        showDetailViewModel.show.observe(viewLifecycleOwner, Observer { show: Show ->
            adapter.update(mutableListOf(itemShowDetailFactory.create(show)))
        })
    }

}

@Module
abstract class ShowDetailFragmentModule {
    companion object {
        @PageScope
        @Provides
        fun providesLifecycleOwnerLiveData(
            showDetailFragment: ShowDetailFragment
        ): LiveData<LifecycleOwner> {
            return showDetailFragment.viewLifecycleOwnerLiveData
        }
    }
}