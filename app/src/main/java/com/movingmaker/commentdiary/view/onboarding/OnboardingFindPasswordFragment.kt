package com.movingmaker.commentdiary.view.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.movingmaker.commentdiary.R
import com.movingmaker.commentdiary.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentOnboardingFindPasswordBinding
import com.movingmaker.commentdiary.viewmodel.onboarding.OnboardingViewModel

class OnboardingFindPasswordFragment : BaseFragment() {
    override val TAG: String = OnboardingFindPasswordFragment::class.java.simpleName

    private val onboardingViewModel: OnboardingViewModel by activityViewModels<OnboardingViewModel>()

    private lateinit var binding: FragmentOnboardingFindPasswordBinding

    companion object {
        fun newInstance(): OnboardingFindPasswordFragment {
            return OnboardingFindPasswordFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentOnboardingFindPasswordBinding.inflate(layoutInflater)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
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
        toolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_24)
        toolbar.setNavigationOnClickListener {
            onboardingViewModel.setCurrentFragment("login")
        }
        findPasswordEditText.addTextChangedListener {
            onboardingViewModel.setFindPasswordEmail(findPasswordEditText.text.toString())
        }

    }
}
