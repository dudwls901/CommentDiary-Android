package com.movingmaker.commentdiary.view.onboarding

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.global.base.BaseFragment
import com.movingmaker.commentdiary.global.CodaSnackBar
import com.movingmaker.commentdiary.data.remote.RetrofitClient
import com.movingmaker.commentdiary.databinding.FragmentOnboardingSignUpEmailBinding
import com.movingmaker.commentdiary.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.viewmodel.onboarding.OnboardingViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class OnboardingSignUpEmailFragment : BaseFragment<FragmentOnboardingSignUpEmailBinding>(R.layout.fragment_onboarding_sign_up_email), CoroutineScope {
    override val TAG: String = OnboardingSignUpEmailFragment::class.java.simpleName
    private val onboardingViewModel: OnboardingViewModel by activityViewModels()
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

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
        onboardingViewModel.shakeView.observe(viewLifecycleOwner){
            if(it) {
                val shortShake: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.shake_short)
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