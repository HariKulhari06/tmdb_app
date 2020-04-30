package com.hari.tmdb.authentication.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.hari.tmdb.authentication.R
import com.hari.tmdb.authentication.databinding.FragmentLoginBinding
import com.hari.tmdb.authentication.viewmodel.LoginViewModel
import com.hari.tmdb.di.Injectable
import com.hari.tmdb.ext.assistedActivityViewModels
import com.hari.tmdb.ext.toAppError
import com.hari.tmdb.system.viewmodel.SystemViewModel
import javax.inject.Inject
import javax.inject.Provider

class LoginFragment : Fragment(R.layout.fragment_login), Injectable {

    @Inject
    lateinit var systemViewModelProvider: Provider<SystemViewModel>
    private val systemViewModel: SystemViewModel by assistedActivityViewModels {
        systemViewModelProvider.get()
    }

    @Inject
    lateinit var loginViewModelProvider: Provider<LoginViewModel>
    private val loginViewModel: LoginViewModel by assistedActivityViewModels {
        loginViewModelProvider.get()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentLoginBinding.bind(view)

        binding.loginButton.setOnClickListener {
            val username = binding.textUsername.text.toString().trim()
            val password = binding.textPassword.text.toString().trim()
            binding.textUsername.addTextChangedListener {
                binding.usernameInputLayout.error = ""
            }
            binding.textPassword.addTextChangedListener {
                binding.passwordInputLayout.error = ""
            }
            if (validate(binding, username, password)) {
                loginViewModel.login(username, password)
                    .observe(viewLifecycleOwner, Observer { loginState ->
                        binding.progressView.isVisible = loginState.isLoading

                        loginState.getErrorIfExists().toAppError()?.let { error ->
                            systemViewModel.onError(error)
                        }
                        loginState.getValueOrNull()?.let { session ->
                            if (session.success)
                                findNavController().navigate(R.id.action_login_to_main)
                        }

                    })
            }
        }

        binding.guestLoginButton.setOnClickListener {
            loginViewModel.loginAsGuest().observe(viewLifecycleOwner, Observer { guestLoginState ->

                binding.progressView.isVisible = guestLoginState.isLoading

                guestLoginState.getErrorIfExists().toAppError()?.let { error ->
                    systemViewModel.onError(error)
                }

                guestLoginState.getValueOrNull()?.let { session ->
                    if (session.success)
                        findNavController().navigate(R.id.action_login_to_main)
                }
            })
        }

    }

    private fun validate(
        binding: FragmentLoginBinding,
        username: String,
        password: String
    ): Boolean {
        return when {
            username.isEmpty() -> {
                binding.usernameInputLayout.error = getString(R.string.username_required)
                false
            }
            password.isEmpty() -> {
                binding.passwordInputLayout.error = getString(R.string.password_required)
                false
            }
            else -> {
                binding.usernameInputLayout.error = ""
                binding.passwordInputLayout.error = ""
                true
            }
        }
    }

}