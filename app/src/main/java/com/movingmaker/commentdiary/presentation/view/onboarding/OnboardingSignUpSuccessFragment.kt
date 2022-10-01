package com.movingmaker.commentdiary.presentation.view.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.common.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentOnboardingSignUpSuccessBinding
import com.movingmaker.commentdiary.common.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.presentation.viewmodel.onboarding.OnboardingViewModel

class OnboardingSignUpSuccessFragment: BaseFragment<FragmentOnboardingSignUpSuccessBinding>(R.layout.fragment_onboarding_sign_up_success) {
    override val TAG: String = OnboardingSignUpSuccessFragment::class.java.simpleName
    private val onboardingViewModel: OnboardingViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onboardingViewModel.setCurrentFragment(FRAGMENT_NAME.SIGNUP_SUCCESS)
    }
}