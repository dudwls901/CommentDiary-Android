package com.movingmaker.presentation.view.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.movingmaker.presentation.R
import com.movingmaker.presentation.base.BaseFragment
import com.movingmaker.presentation.databinding.FragmentOnboardingLoginBinding
import com.movingmaker.presentation.view.main.MainActivity
import com.movingmaker.presentation.view.snackbar.CodaSnackBar
import com.movingmaker.presentation.viewmodel.onboarding.LoginScreenUiEvent
import com.movingmaker.presentation.viewmodel.onboarding.OnboardingLoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class OnboardingLoginFragment :
    BaseFragment<FragmentOnboardingLoginBinding>(R.layout.fragment_onboarding_login) {

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { res ->
        onSignInResult(res)
    }

    private val viewmodel: OnboardingLoginViewModel by viewModels()

    private val signInIntent: Intent by lazy {
        AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(listOf(AuthUI.IdpConfig.GoogleBuilder().build()))
            .setLogo(R.drawable.img_logo)
            .setTheme(R.style.Onboarding)
            .build()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewmodel
        observeDatas()
    }

    private fun observeDatas() {
        viewmodel.uiEvent
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { handleEvent(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleEvent(event: LoginScreenUiEvent) {
        when (event) {
            LoginScreenUiEvent.Login.Start -> {
                signInLauncher.launch(signInIntent)
            }

            LoginScreenUiEvent.Login.Fail -> {
                CodaSnackBar.make(binding.root, "오류가 발생했습니다.")
            }

            LoginScreenUiEvent.Login.Success -> {
                startActivity(Intent(requireContext(), MainActivity::class.java).apply {
                    requireActivity().finish()
                })
            }
        }
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            viewmodel.registerUser(user)
        } else {
            CodaSnackBar.make(binding.root, "오류가 발생했습니다.")
        }
    }
}