package com.movingmaker.commentdiary.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.movingmaker.commentdiary.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentOnboardingLoginBinding
import com.movingmaker.commentdiary.viewmodel.onboarding.OnboardingViewModel


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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        onboardingSignUpFragment = OnboardingSignUpFragment.newInstance()
        binding.onboardingviewModel = onboardingViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        initViews()

        return binding.root
    }

    private fun initViews() = with(binding){

        emailEditText.addTextChangedListener {
            onboardingViewModel.setEmail(emailEditText.text.toString())
        }

        passwordEditText.addTextChangedListener {
            onboardingViewModel.setPassword(passwordEditText.text.toString())
        }

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