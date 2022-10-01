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
import com.movingmaker.commentdiary.databinding.FragmentOnboardingSignUpCodeBinding
import com.movingmaker.commentdiary.common.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.presentation.viewmodel.onboarding.OnboardingViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class OnboardingSignUpCodeFragment : BaseFragment<FragmentOnboardingSignUpCodeBinding>(R.layout.fragment_onboarding_sign_up_code), CoroutineScope {
    override val TAG: String = OnboardingSignUpCodeFragment::class.java.simpleName

    private val onboardingViewModel: OnboardingViewModel by activityViewModels()
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onboardingViewModel.setShakeView(false)
        onboardingViewModel.setCodeCorrect(true)
        onboardingViewModel.setCurrentFragment(FRAGMENT_NAME.SIGNUP_CODE)
        binding.vm = onboardingViewModel
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        initViews()
        observeDatas()
    }

    private fun observeDatas() {
        onboardingViewModel.shakeView.observe(viewLifecycleOwner){
            if(it) {
                val shortShake: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.shake_short)
                binding.codeNoticeTextView.startAnimation(shortShake)
            }
        }
    }

    private fun initViews() = with(binding) {
        codeEditText.addTextChangedListener {
            onboardingViewModel.setEmailCode(codeEditText.text.toString())
        }
    }

}