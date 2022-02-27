package com.movingmaker.commentdiary.view.onboarding

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import com.movingmaker.commentdiary.BaseFragment
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.databinding.FragmentOnboardingLoginBinding
import com.movingmaker.commentdiary.view.OnboardingViewModel

class OnboardingLoginFragment: BaseFragment() {
    override val TAG: String = OnboardingLoginFragment::class.java.simpleName

    private lateinit var binding: FragmentOnboardingLoginBinding
    private lateinit var onboardingSignUpFragment: OnboardingSignUpFragment
    private val onboardingViewModel: OnboardingViewModel by activityViewModels()
    companion object{
        fun newInstance(): OnboardingLoginFragment{
            return OnboardingLoginFragment()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentOnboardingLoginBinding.inflate(layoutInflater)
        onboardingSignUpFragment = OnboardingSignUpFragment.newInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViews()

        return binding.root
    }

    private fun initViews() = with(binding){

        makeAccountTextView.setOnClickListener {
//            val loginActivity = activity as OnboardingLoginActivity
//            loginActivity.changeFragment(1)
            onboardingViewModel.setCurrentFragment("signUp")
        }
        findPasswordTextView.setOnClickListener {
            onboardingViewModel.setCurrentFragment("findPW")
        }
    }



}