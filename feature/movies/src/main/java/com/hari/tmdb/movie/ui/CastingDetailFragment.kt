package com.hari.tmdb.movie.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialContainerTransform
import com.hari.tmdb.movie.R
import com.hari.tmdb.movie.databinding.CastingProfileFragmentBinding

class CastingDetailFragment : Fragment(R.layout.casting_profile_fragment) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform(requireContext()).apply {
            drawingViewId = R.id.casting_root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = CastingProfileFragmentBinding.bind(view)
    }
}
