package com.movingmaker.commentdiary.view.onboarding

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.util.Linkify
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.global.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentOnboardingSignUpTermsBinding
import com.movingmaker.commentdiary.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.util.Url.POLICY_URL
import com.movingmaker.commentdiary.util.Url.TERMS_URL
import com.movingmaker.commentdiary.viewmodel.onboarding.OnboardingViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class OnboardingSignUpTermsFragment : BaseFragment<FragmentOnboardingSignUpTermsBinding>(R.layout.fragment_onboarding_sign_up_terms) {
    override val TAG: String = OnboardingSignUpTermsFragment::class.java.simpleName

    private val onboardingViewModel: OnboardingViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = onboardingViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        onboardingViewModel.setCurrentFragment(FRAGMENT_NAME.SIGNUP_TERMS)
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
            findNavController().popBackStack()
        }

    }

}