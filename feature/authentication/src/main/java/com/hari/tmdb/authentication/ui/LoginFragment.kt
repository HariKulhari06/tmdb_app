package com.hari.tmdb.authentication.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hari.tmdb.authentication.R
import com.hari.tmdb.authentication.databinding.FragmentLoginBinding
import com.hari.tmdb.authentication.ui.LoginFragmentDirections.Companion.actionLoginToMain

class LoginFragment : Fragment(R.layout.fragment_login) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentLoginBinding.bind(view)

        binding.loginButton.setOnClickListener { findNavController().navigate(actionLoginToMain()) }
    }

}