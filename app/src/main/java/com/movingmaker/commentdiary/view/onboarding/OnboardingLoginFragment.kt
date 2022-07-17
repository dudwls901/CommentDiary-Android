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


class OnboardingLoginFragment: BaseFragment() {
    override val TAG: String = OnboardingLoginFragment::class.java.simpleName

    private lateinit var binding: FragmentOnboardingLoginBinding
    private val onboardingViewModel: OnboardingViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentOnboardingLoginBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        onboardingViewModel.setShakeView(false)
        onboardingViewModel.setLoginNotice("")
        onboardingViewModel.setCurrentFragment(FRAGMENT_NAME.LOGIN)
        binding.vm = onboardingViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initViews()
        observeDatas()
        return binding.root
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