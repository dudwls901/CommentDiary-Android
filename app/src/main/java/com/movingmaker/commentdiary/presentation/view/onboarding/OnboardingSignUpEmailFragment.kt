package com.movingmaker.commentdiary.presentation.view.onboarding

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.common.base.BaseFragment
import com.movingmaker.commentdiary.common.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.databinding.FragmentOnboardingSignUpEmailBinding
import com.movingmaker.commentdiary.presentation.viewmodel.onboarding.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingSignUpEmailFragment :
    BaseFragment<FragmentOnboardingSignUpEmailBinding>(R.layout.fragment_onboarding_sign_up_email) {
    override val TAG: String = OnboardingSignUpEmailFragment::class.java.simpleName
    private val onboardingViewModel: OnboardingViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onboardingViewModel.setShakeView(false)
        onboardingViewModel.setEmailNotice("")
        onboardingViewModel.setEmailCorrect(true)
        onboardingViewModel.setCurrentFragment(FRAGMENT_NAME.SIGNUP_EMAIL)
        binding.vm = onboardingViewModel
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        initViews()
        observeDatas()

    }

    private fun observeDatas() {
        onboardingViewModel.shakeView.observe(viewLifecycleOwner) {
            if (it) {
                val shortShake: Animation =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.shake_short)
                binding.emailNoticeTextView.startAnimation(shortShake)
            }
        }
    }

    private fun initViews() = with(binding) {

        emailEditText.addTextChangedListener {
            onboardingViewModel.setEmail(emailEditText.text.toString())
            onboardingViewModel.validateEmail("email")
        }
    }

}