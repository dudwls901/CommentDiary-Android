package com.movingmaker.commentdiary.view.onboarding

import android.content.Intent
import android.net.Uri
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
import com.movingmaker.commentdiary.databinding.FragmentOnboardingKakaoTermsBinding
import com.movingmaker.commentdiary.databinding.FragmentOnboardingSignUpEmailBinding
import com.movingmaker.commentdiary.databinding.FragmentOnboardingSignUpTermsBinding
import com.movingmaker.commentdiary.util.FRAGMENT_NAME
import com.movingmaker.commentdiary.util.Url
import com.movingmaker.commentdiary.view.main.MainActivity
import com.movingmaker.commentdiary.viewmodel.onboarding.OnboardingViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class OnboardingKakaoTermsFragment : BaseFragment(),CoroutineScope {
    override val TAG: String = OnboardingKakaoTermsFragment::class.java.simpleName

    private val onboardingViewModel: OnboardingViewModel by activityViewModels()


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private lateinit var binding: FragmentOnboardingKakaoTermsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentOnboardingKakaoTermsBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.vm = onboardingViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        onboardingViewModel.setCurrentFragment(FRAGMENT_NAME.KAKAO_TERMS)
        if(onboardingViewModel.isAllAccept.value == true){
            onboardingViewModel.setAllAccept()
        }
        else{
            onboardingViewModel.setAllAccept()
            onboardingViewModel.setAllAccept()
        }
        initViews()
        return binding.root
    }

    private fun initViews() = with(binding) {

        binding.allAcceptsLayout.setOnClickListener {
            onboardingViewModel.setAllAccept()
        }
        binding.termsAcceptLayout.setOnClickListener {
            onboardingViewModel.setTermsAccept()
        }
        binding.policyAcceptLayout.setOnClickListener {
            onboardingViewModel.setPolicyAccept()
        }
        binding.pushAcceptLayout.setOnClickListener {
            onboardingViewModel.setPushAccept()
        }

        binding.goPolicyButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Url.POLICY_URL)))
        }
        binding.goTermsButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Url.TERMS_URL)))
        }

        binding.backButton.setOnClickListener {
             launch {
                onboardingViewModel.onLoading()
                if (onboardingViewModel.signOut()) {
                    findNavController().popBackStack()
                }
            }
        }
    }
}