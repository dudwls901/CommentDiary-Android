package com.movingmaker.commentdiary.view.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.databinding.FragmentOnboardingLoginBeforeBinding

class OnboardingLoginBeforeFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingLoginBeforeBinding

    companion object{
        fun newInstance(): OnboardingLoginBeforeFragment{
            return OnboardingLoginBeforeFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentOnboardingLoginBeforeBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }
}