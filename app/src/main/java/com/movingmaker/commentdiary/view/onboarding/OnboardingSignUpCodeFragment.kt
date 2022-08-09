package com.movingmaker.commentdiary.view.onboarding

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.movingmaker.commentdiary.global.CodaApplication
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.global.base.BaseFragment
import com.movingmaker.commentdiary.global.CodaSnackBar
import com.movingmaker.commentdiary.data.remote.RetrofitClient
import com.movingmaker.commentdiary.databinding.FragmentOnboardingSignUpCodeBinding
import com.movingmaker.commentdiary.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.viewmodel.onboarding.OnboardingViewModel
import kotlinx.coroutines.*
import java.lang.NumberFormatException
import java.util.regex.Pattern
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