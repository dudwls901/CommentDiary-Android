package com.movingmaker.commentdiary.presentation.view.onboarding


import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.databinding.FragmentOnboardingSignUpPasswordBinding
import com.movingmaker.commentdiary.presentation.base.BaseFragment
import com.movingmaker.commentdiary.presentation.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.presentation.viewmodel.onboarding.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingSignUpPasswordFragment :
    BaseFragment<FragmentOnboardingSignUpPasswordBinding>(R.layout.fragment_onboarding_sign_up_password) {
    private val onboardingViewModel: OnboardingViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = onboardingViewModel
        initViews()
        observeDatas()
    }

    private fun initViews() {
        with(onboardingViewModel) {
            setShakeView(false)
            clearPassword()
            clearCheckPassword()
            validatePassword()
            validateCheckPassword()
            setCurrentFragment(FRAGMENT_NAME.SIGNUP_PASSWORD)
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeDatas() {
        onboardingViewModel.shakeView.observe(viewLifecycleOwner) {
            if (it) {
                val shortShake: Animation =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.shake_short)
                binding.apply {
                    if (onboardingViewModel.passwordCorrect.value == false) {
                        passwordNoticeTextView.startAnimation(shortShake)
                    }
                    if (onboardingViewModel.passwordCheckCorrect.value == false) {
                        passwordCheckNoticeTextView.startAnimation(shortShake)
                    }
                }
            }
        }
        onboardingViewModel.password.observe(viewLifecycleOwner) {
            onboardingViewModel.validatePassword()
        }
        onboardingViewModel.checkPassword.observe(viewLifecycleOwner) {
            onboardingViewModel.validateCheckPassword()
        }
    }
}