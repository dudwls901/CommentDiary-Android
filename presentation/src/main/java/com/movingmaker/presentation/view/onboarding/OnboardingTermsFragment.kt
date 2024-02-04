package com.movingmaker.presentation.view.onboarding

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.movingmaker.presentation.R
import com.movingmaker.presentation.base.BaseFragment
import com.movingmaker.presentation.databinding.FragmentOnboardingSignUpTermsBinding
import com.movingmaker.presentation.util.POLICY_URL
import com.movingmaker.presentation.util.TERMS_URL
import com.movingmaker.presentation.viewmodel.onboarding.OnboardingTermsViewModel
import com.movingmaker.presentation.viewmodel.onboarding.TermsScreenUiEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class OnboardingTermsFragment :
    BaseFragment<FragmentOnboardingSignUpTermsBinding>(R.layout.fragment_onboarding_sign_up_terms) {

    private val viewModel: OnboardingTermsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        observeDatas()
    }

    private fun observeDatas() {
        viewModel.uiEvent
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { handleEvent(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleEvent(event: TermsScreenUiEvent) {
        when (event) {
            TermsScreenUiEvent.GoPolicy -> {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(POLICY_URL)))
            }

            TermsScreenUiEvent.GoTerms -> {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(TERMS_URL)))
            }

            is TermsScreenUiEvent.Submit -> {
                findNavController().navigate(OnboardingTermsFragmentDirections.actionOnboardingSignUpTermsFragmentToOnboardingLoginFragment(event.isPushAccept))
            }
        }
    }
}