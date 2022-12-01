package com.movingmaker.commentdiary.presentation.view.onboarding

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.databinding.FragmentOnboardingSignUpCodeBinding
import com.movingmaker.commentdiary.presentation.base.BaseFragment
import com.movingmaker.commentdiary.presentation.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.presentation.viewmodel.onboarding.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingSignUpCodeFragment :
    BaseFragment<FragmentOnboardingSignUpCodeBinding>(R.layout.fragment_onboarding_sign_up_code) {

    private val onboardingViewModel: OnboardingViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onboardingViewModel.setShakeView(false)
        onboardingViewModel.setCodeCorrect(true)
        onboardingViewModel.clearCode()
        onboardingViewModel.setCurrentFragment(FRAGMENT_NAME.SIGNUP_CODE)
        binding.vm = onboardingViewModel
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        observeDatas()
    }

    private fun observeDatas() {
        onboardingViewModel.shakeView.observe(viewLifecycleOwner) {
            if (it) {
                val shortShake: Animation =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.shake_short)
                binding.codeNoticeTextView.startAnimation(shortShake)
            }
        }
    }
}