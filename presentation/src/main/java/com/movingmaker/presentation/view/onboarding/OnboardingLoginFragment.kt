package com.movingmaker.presentation.view.onboarding

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.movingmaker.presentation.R
import com.movingmaker.presentation.base.BaseFragment
import com.movingmaker.presentation.databinding.FragmentOnboardingLoginBinding
import com.movingmaker.presentation.util.FRAGMENT_NAME
import com.movingmaker.presentation.viewmodel.onboarding.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingLoginFragment :
    BaseFragment<FragmentOnboardingLoginBinding>(R.layout.fragment_onboarding_login) {

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
            setLoginNotice("")
            setCurrentFragment(FRAGMENT_NAME.LOGIN)
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
                binding.loginNoticeTextView.startAnimation(shortShake)
            }
        }
    }
}