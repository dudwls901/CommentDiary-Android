package com.movingmaker.commentdiary.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.movingmaker.commentdiary.global.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentOnboardingSignUpSuccessBinding
import com.movingmaker.commentdiary.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.viewmodel.onboarding.OnboardingViewModel

class OnboardingSignUpSuccessFragment: BaseFragment() {
    override val TAG: String = OnboardingSignUpSuccessFragment::class.java.simpleName

    private lateinit var binding: FragmentOnboardingSignUpSuccessBinding

    private val onboardingViewModel: OnboardingViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentOnboardingSignUpSuccessBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        onboardingViewModel.setCurrentFragment(FRAGMENT_NAME.SIGNUP_SUCCESS)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}