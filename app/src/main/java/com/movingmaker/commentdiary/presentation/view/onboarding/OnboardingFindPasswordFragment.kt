package com.movingmaker.commentdiary.presentation.view.onboarding

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.common.base.BaseFragment
import com.movingmaker.commentdiary.common.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.databinding.FragmentOnboardingFindPasswordBinding
import com.movingmaker.commentdiary.presentation.viewmodel.onboarding.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFindPasswordFragment :
    BaseFragment<FragmentOnboardingFindPasswordBinding>(R.layout.fragment_onboarding_find_password) {
    override val TAG: String = OnboardingFindPasswordFragment::class.java.simpleName

    private val onboardingViewModel: OnboardingViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onboardingViewModel.setCurrentFragment(FRAGMENT_NAME.FIND_PASSWORD)
        //화면 재진입 시 notice 텍스트 초기화를 위함
        onboardingViewModel.setShakeView(false)
        onboardingViewModel.setFindPasswordEmailCorrect(true)
        onboardingViewModel.setFindPasswordEmailNotice("")
        binding.vm = onboardingViewModel
        initViews()
        observeDatas()
    }

    @SuppressLint("ResourceAsColor")
    private fun observeDatas() {
        onboardingViewModel.shakeView.observe(viewLifecycleOwner) {
            if (it) {
                val shortShake: Animation =
                    AnimationUtils.loadAnimation(requireContext(), R.anim.shake_short)
                binding.emailIncorrectTextView.startAnimation(shortShake)
            }
        }
        onboardingViewModel.successFindPassword.observe(viewLifecycleOwner) {
            if (it) {
                binding.emailIncorrectTextView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.core_green
                    )
                )
                binding.emailIncorrectTextView.text = getString(R.string.onboarding_password_send)
                binding.emailIncorrectTextView.visibility = View.VISIBLE
            }
        }
        onboardingViewModel.findPasswordEmailNotice.observe(viewLifecycleOwner) {
            binding.emailIncorrectTextView.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.core_notice
                )
            )
            binding.emailIncorrectTextView.text = it
            binding.emailIncorrectTextView.visibility = View.VISIBLE
        }
    }

    private fun initViews() = with(binding) {
        backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        emailEditText.addTextChangedListener {
            onboardingViewModel.setFindPasswordEmail(emailEditText.text.toString())
            onboardingViewModel.validateEmail("findPasswordEmail")
        }

    }
}