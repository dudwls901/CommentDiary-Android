package com.movingmaker.presentation.view.onboarding

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.movingmaker.presentation.R
import com.movingmaker.presentation.base.BaseFragment
import com.movingmaker.presentation.databinding.FragmentOnboardingSignUpTermsBinding
import com.movingmaker.presentation.util.FRAGMENT_NAME
import com.movingmaker.presentation.util.POLICY_URL
import com.movingmaker.presentation.util.TERMS_URL
import com.movingmaker.presentation.viewmodel.onboarding.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingSignUpTermsFragment :
    BaseFragment<FragmentOnboardingSignUpTermsBinding>(R.layout.fragment_onboarding_sign_up_terms) {

    private val onboardingViewModel: OnboardingViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = onboardingViewModel
        initViews()
    }

    private fun initViews() = with(binding) {

        onboardingViewModel.setCurrentFragment(FRAGMENT_NAME.SIGNUP_TERMS)

        binding.allAcceptsLayout.setOnClickListener {
            onboardingViewModel.setAllAccept()
        }
        binding.termsAcceptLayout.setOnClickListener {
            onboardingViewModel.setTermsAccept()
        }
        binding.policyAcceptLayout.setOnClickListener {
            onboardingViewModel.setPolicyAccept()
        }
        binding.pushAcceptLayout.setOnClickListener {
            onboardingViewModel.setPushAccept()
        }

        binding.goPolicyButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(POLICY_URL)))
        }
        binding.goTermsButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(TERMS_URL)))
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

    }

}