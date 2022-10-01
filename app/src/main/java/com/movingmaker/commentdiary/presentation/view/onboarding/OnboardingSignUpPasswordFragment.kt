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
import com.movingmaker.commentdiary.databinding.FragmentOnboardingSignUpPasswordBinding
import com.movingmaker.commentdiary.common.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.presentation.viewmodel.onboarding.OnboardingViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class OnboardingSignUpPasswordFragment : BaseFragment<FragmentOnboardingSignUpPasswordBinding>(R.layout.fragment_onboarding_sign_up_password), CoroutineScope {
    override val TAG: String = OnboardingSignUpPasswordFragment::class.java.simpleName
    private val onboardingViewModel: OnboardingViewModel by activityViewModels()
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onboardingViewModel.setShakeView(false)
        onboardingViewModel.setPassword("")
        onboardingViewModel.setCheckPassword("")
        onboardingViewModel.validatePassword()
        onboardingViewModel.validateCheckPassword()
        onboardingViewModel.setCurrentFragment(FRAGMENT_NAME.SIGNUP_PASSWORD)
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
               binding.apply {
                   if(onboardingViewModel.passwordCorrect.value == false){
                       passwordNoticeTextView.startAnimation(shortShake)
                   }
                   if(onboardingViewModel.passwordCheckCorrect.value == false){
                       passwordCheckNoticeTextView.startAnimation(shortShake)
                   }
               }
            }
        }
    }

    private fun initViews() = with(binding) {

        passwordEditText.addTextChangedListener {
            onboardingViewModel.setPassword(passwordEditText.text.toString())
            onboardingViewModel.validatePassword()
        }

        passwordCheckEditText.addTextChangedListener {
            onboardingViewModel.setCheckPassword(passwordCheckEditText.text.toString())
            onboardingViewModel.validateCheckPassword()
        }
    }
}