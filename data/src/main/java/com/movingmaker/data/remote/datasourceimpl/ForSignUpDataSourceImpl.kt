package com.movingmaker.data.remote.datasourceimpl

import com.movingmaker.data.remote.api.OnboardingApiService
import com.movingmaker.data.remote.datasource.ForSignUpDataSource
import com.movingmaker.data.remote.model.request.EmailCodeCheckRequest
import com.movingmaker.data.remote.model.request.KakaoLoginRequest
import com.movingmaker.data.remote.model.request.KakaoSignUpRequest
import com.movingmaker.data.remote.model.request.LogInRequest
import com.movingmaker.data.remote.model.request.SignUpRequest
import com.movingmaker.data.util.safeApiCall
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.response.AuthTokens
import com.movingmaker.domain.model.response.BaseResponse
import javax.inject.Inject

class ForSignUpDataSourceImpl @Inject constructor(
    private val onboardingApiService: OnboardingApiService
) : ForSignUpDataSource {
    override suspend fun sendEmailCode(email: String): NetworkResult<BaseResponse<String>> =
        safeApiCall { onboardingApiService.sendEmailCode(email) }

    override suspend fun emailCodeCheck(emailCodeCheckRequest: EmailCodeCheckRequest): NetworkResult<BaseResponse<String>> =
        safeApiCall { onboardingApiService.emailCodeCheck(emailCodeCheckRequest) }

    override suspend fun signUp(signUpRequest: SignUpRequest): NetworkResult<BaseResponse<String>> =
        safeApiCall { onboardingApiService.signUp(signUpRequest) }

    override suspend fun kakaoLogIn(kakaoLoginRequest: KakaoLoginRequest): NetworkResult<BaseResponse<AuthTokens>> =
        safeApiCall { onboardingApiService.kakaoLogIn(kakaoLoginRequest) }

    override suspend fun findPassword(email: String): NetworkResult<BaseResponse<String>> =
        safeApiCall { onboardingApiService.findPassword(email) }

    override suspend fun logIn(logInRequest: LogInRequest): NetworkResult<BaseResponse<AuthTokens>> =
        safeApiCall { onboardingApiService.logIn(logInRequest) }

    override suspend fun kakaoSignUpSetAccepts(kakaoSignUpRequest: KakaoSignUpRequest): NetworkResult<BaseResponse<String>> =
        safeApiCall { onboardingApiService.kakaoSignUpSetAccepts(kakaoSignUpRequest) }

}