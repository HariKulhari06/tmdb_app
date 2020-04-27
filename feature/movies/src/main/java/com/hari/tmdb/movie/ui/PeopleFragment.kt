package com.hari.tmdb.movie.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import com.hari.tmdb.di.Injectable
import com.hari.tmdb.di.PageScope
import com.hari.tmdb.ext.assistedActivityViewModels
import com.hari.tmdb.ext.assistedViewModels
import com.hari.tmdb.movie.R
import com.hari.tmdb.movie.databinding.PeopleFragmentBinding
import com.hari.tmdb.movie.item.PeopleDetailItem
import com.hari.tmdb.movie.viewmodel.PeopleViewModel
import com.hari.tmdb.system.viewmodel.SystemViewModel
import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.databinding.GroupieViewHolder
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Provider

class PeopleFragment : Fragment(R.layout.people_fragment), Injectable {

    @Inject
    lateinit var systemViewModelFactory: Provider<SystemViewModel>
    private val systemViewModel by assistedActivityViewModels {
        systemViewModelFactory.get()
    }

    @Inject
    lateinit var peopleViewModelFactory: PeopleViewModel.Factory
    private val peopleViewModel by assistedViewModels {
        peopleViewModelFactory.create(navArgs.peopleId)
    }

    private val navArgs: PeopleFragmentArgs by navArgs()

    @Inject
    lateinit var peopleDetailItemFactory: PeopleDetailItem.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform(requireContext()).apply {
            drawingViewId = R.id.people_root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = PeopleFragmentBinding.bind(view)
        binding.peopleRoot.transitionName = "${navArgs.peopleId}-${navArgs.transitionNameSuffix}"

        val adapter = GroupAdapter<GroupieViewHolder<*>>()
        binding.peopleRecyclerView.adapter = adapter

        peopleViewModel.ui.observe(viewLifecycleOwner, Observer { uiModel ->
            uiModel.error?.let { error ->
                systemViewModel.onError(error)
            }

            uiModel.people?.let { people ->
                val items = mutableListOf<Group>()
                items += peopleDetailItemFactory.create(people)
                adapter.update(items)

            }
        })
    }
}

@Module
abstract class PeopleFragmentModule {
    companion object {
        @PageScope
        @Provides
        fun providesLifecycleOwnerLiveData(
            peopleFragment: PeopleFragment
        ): LiveData<LifecycleOwner> {
            return peopleFragment.viewLifecycleOwnerLiveData
        }
    }
}
