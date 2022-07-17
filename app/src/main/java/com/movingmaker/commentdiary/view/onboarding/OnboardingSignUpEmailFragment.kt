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

class OnboardingSignUpEmailFragment : BaseFragment(), CoroutineScope {
    override val TAG: String = OnboardingSignUpEmailFragment::class.java.simpleName

    private val onboardingViewModel: OnboardingViewModel by activityViewModels()

    private lateinit var binding: FragmentOnboardingSignUpEmailBinding

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentOnboardingSignUpEmailBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        onboardingViewModel.setShakeView(false)
        onboardingViewModel.setEmailNotice("")
        onboardingViewModel.setEmailCorrect(true)
        onboardingViewModel.setCurrentFragment(FRAGMENT_NAME.SIGNUP_EMAIL)
        binding.vm = onboardingViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        initViews()
        observeDatas()

//todo dddd

//        val mTransform: Linkify.TransformFilter = Linkify.TransformFilter { matcher, s ->
//            ""
//        }
//        val pattern1 = Pattern.compile("개인정보 취급 방침")
//        val pattern2 = Pattern.compile("이용 약관")
//        Linkify.addLinks(binding.signUpAgreeNoticeTextView,pattern1,
//            CodaApplication.policyUrl, null, mTransform )
//        Linkify.addLinks(binding.signUpAgreeNoticeTextView,pattern2,
//            CodaApplication.termsUrl, null, mTransform )


        return binding.root
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