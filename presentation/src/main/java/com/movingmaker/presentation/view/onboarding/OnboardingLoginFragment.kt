package com.movingmaker.presentation.view.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.movingmaker.presentation.R
import com.movingmaker.presentation.base.BaseFragment
import com.movingmaker.presentation.databinding.FragmentOnboardingLoginBinding
import com.movingmaker.presentation.util.setOnSingleClickListener
import com.movingmaker.presentation.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class OnboardingLoginFragment :
    BaseFragment<FragmentOnboardingLoginBinding>(R.layout.fragment_onboarding_login) {

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { res ->
        onSignInResult(res)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {

        binding.googleLoginButton.setOnSingleClickListener {
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(listOf(AuthUI.IdpConfig.GoogleBuilder().build()))
                .setLogo(R.drawable.img_logo)
                .setTheme(R.style.Onboarding)
                .build()
            signInLauncher.launch(signInIntent)
        }
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            Timber.e("영진 $user $response")
            startActivity(Intent(requireContext(), MainActivity::class.java).apply {
                requireActivity().finish()
            })
        } else {
            Timber.e("실패")
        }
    }
}