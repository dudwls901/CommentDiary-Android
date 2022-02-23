package com.movingmaker.commentdiary.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.movingmaker.commentdiary.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentOnboardingSignUpSuccessBinding
import com.movingmaker.commentdiary.view.main.Fragment3

class OnboardingSignUpSuccessFragment: BaseFragment() {
    override val TAG: String = OnboardingSignUpSuccessFragment::class.java.simpleName

    private lateinit var binding: FragmentOnboardingSignUpSuccessBinding

    companion object{
        fun newInstance() : OnboardingSignUpSuccessFragment {
            return OnboardingSignUpSuccessFragment()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentOnboardingSignUpSuccessBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}