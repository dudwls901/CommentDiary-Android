package com.movingmaker.presentation.view.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.movingmaker.presentation.R
import com.movingmaker.presentation.base.BaseFragment
import com.movingmaker.presentation.databinding.FragmentOnboardingLoginBeforeBinding
import com.movingmaker.presentation.util.FRAGMENT_NAME
import com.movingmaker.presentation.view.main.MainActivity
import com.movingmaker.presentation.viewmodel.onboarding.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class OnboardingLoginBeforeFragment :
    BaseFragment<FragmentOnboardingLoginBeforeBinding>(R.layout.fragment_onboarding_login_before) {

    private val onboardingViewModel: OnboardingViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {

        onboardingViewModel.setCurrentFragment(FRAGMENT_NAME.LOGIN_BEFORE)

        binding.startWithEmailButton.setOnClickListener {
            findNavController().navigate(OnboardingLoginBeforeFragmentDirections.actionOnboardingLoginBeforeFragmentToOnboardingLoginFragment())
        }

        binding.startWithKakaoButton.setOnClickListener {
            // 카카오계정으로 로그인 공통 callback 구성
            // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Timber.e("카카오계정으로 로그인 실패", error)
                } else if (token != null) {
                    Timber.i("카카오계정으로 로그인 성공 ${token.accessToken}")
                    login(token.accessToken)
                }
            }

            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
                UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                    if (error != null) {
                        Timber.e("카카오톡으로 로그인 실패", error)
                        //todo 예외처리
                        Timber.e(
                            "${error is ClientError && error.reason == ClientErrorCause.Cancelled}",
                            error
                        )

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(
                            requireContext(),
                            callback = callback
                        )
                    } else if (token != null) {
                        Timber.i("카카오톡으로 로그인 성공 ${token.accessToken}")
                        login(token.accessToken)
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
            }
        }
    }

    private fun login(kakaoToken: String) {
        lifecycleScope.launch {
            val (isSuccessLogin, isNewMember) = onboardingViewModel.kakaoLogin(kakaoToken)
            if (isSuccessLogin) {
                when (isNewMember) {
                    true -> {
                        findNavController().navigate(OnboardingLoginBeforeFragmentDirections.actionOnboardingLoginBeforeFragmentToOnboardingKakaoTermsFragment())
                    }
                    else -> {
                        startActivity(
                            Intent(
                                requireContext(),
                                MainActivity::class.java
                            ).apply {
                                requireActivity().finish()
                            })
                    }
                }
            }
        }
    }
}