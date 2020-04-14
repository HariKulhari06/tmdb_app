package com.hari.tmdb.authentication.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.hari.tmdb.authentication.R
import com.hari.tmdb.authentication.databinding.FragmentSplashBinding
import com.hari.tmdb.authentication.ui.SplashFragmentDirections.Companion.actionSplashToLogin
import kotlinx.coroutines.delay

class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSplashBinding.bind(view)

        lifecycleScope.launchWhenResumed {
            delay(2000)
            findNavController().navigate(actionSplashToLogin())
        }
    }
}