package com.movingmaker.commentdiary.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.global.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentOnboardingLoginBinding
import com.movingmaker.commentdiary.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.viewmodel.onboarding.OnboardingViewModel


class OnboardingLoginFragment: BaseFragment<FragmentOnboardingLoginBinding>(R.layout.fragment_onboarding_login) {
    override val TAG: String = OnboardingLoginFragment::class.java.simpleName

    private val onboardingViewModel: OnboardingViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onboardingViewModel.setShakeView(false)
        onboardingViewModel.setLoginNotice("")
        onboardingViewModel.setCurrentFragment(FRAGMENT_NAME.LOGIN)
        binding.vm = onboardingViewModel

        initViews()
        observeDatas()
    }

    private fun observeDatas() {
        onboardingViewModel.shakeView.observe(viewLifecycleOwner){
            if(it) {
                val shortShake: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.shake_short)
                binding.loginNoticeTextView.startAnimation(shortShake)
            }
        }
    }

    private fun initViews() = with(binding){
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        emailEditText.addTextChangedListener {
            onboardingViewModel.setEmail(emailEditText.text.toString())
        }

        passwordEditText.addTextChangedListener {
            onboardingViewModel.setPassword(passwordEditText.text.toString())
        }
    }

}