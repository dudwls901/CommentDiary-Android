package com.movingmaker.presentation.view.onboarding

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.movingmaker.presentation.R
import com.movingmaker.presentation.base.BaseFragment
import com.movingmaker.presentation.databinding.FragmentOnboardingKakaoTermsBinding
import com.movingmaker.presentation.util.FRAGMENT_NAME
import com.movingmaker.presentation.util.POLICY_URL
import com.movingmaker.presentation.util.TERMS_URL
import com.movingmaker.presentation.viewmodel.onboarding.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnboardingKakaoTermsFragment :
    BaseFragment<FragmentOnboardingKakaoTermsBinding>(R.layout.fragment_onboarding_kakao_terms) {

    private val onboardingViewModel: OnboardingViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = onboardingViewModel
        onboardingViewModel.setCurrentFragment(FRAGMENT_NAME.KAKAO_TERMS)
        if (onboardingViewModel.isAllAccept.value == true) {
            onboardingViewModel.setAllAccept()
        } else {
            onboardingViewModel.setAllAccept()
            onboardingViewModel.setAllAccept()
        }
        initViews()
    }

    private fun initViews() = with(binding) {

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
            lifecycleScope.launch {
                if (onboardingViewModel.signOut()) {
                    findNavController().popBackStack()
                }
            }
        }
    }
}