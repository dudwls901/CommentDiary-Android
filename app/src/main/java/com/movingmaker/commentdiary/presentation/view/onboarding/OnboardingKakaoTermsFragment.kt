package com.movingmaker.commentdiary.presentation.view.onboarding

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.common.base.BaseFragment
import com.movingmaker.commentdiary.common.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.common.util.Url
import com.movingmaker.commentdiary.databinding.FragmentOnboardingKakaoTermsBinding
import com.movingmaker.commentdiary.presentation.viewmodel.onboarding.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnboardingKakaoTermsFragment :
    BaseFragment<FragmentOnboardingKakaoTermsBinding>(R.layout.fragment_onboarding_kakao_terms) {
    override val TAG: String = OnboardingKakaoTermsFragment::class.java.simpleName

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
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Url.POLICY_URL)))
        }
        binding.goTermsButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Url.TERMS_URL)))
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