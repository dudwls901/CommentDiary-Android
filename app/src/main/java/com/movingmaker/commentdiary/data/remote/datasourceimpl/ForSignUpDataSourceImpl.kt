package com.movingmaker.commentdiary.data.remote.datasourceimpl

import com.movingmaker.commentdiary.data.model.AuthTokens
import com.movingmaker.commentdiary.data.remote.api.OnboardingApiService
import com.movingmaker.commentdiary.data.remote.datasource.ForSignUpDataSource
import com.movingmaker.commentdiary.data.remote.request.EmailCodeCheckRequest
import com.movingmaker.commentdiary.data.remote.request.KakaoLoginRequest
import com.movingmaker.commentdiary.data.remote.request.LogInRequest
import com.movingmaker.commentdiary.data.remote.request.SignUpRequest
import com.movingmaker.commentdiary.data.util.safeApiCall
import com.movingmaker.commentdiary.domain.model.BaseResponse
import com.movingmaker.commentdiary.domain.model.NetworkResult
import javax.inject.Inject

class ForSignUpDataSourceImpl @Inject constructor(
    private val onboardingApiService: OnboardingApiService
) : ForSignUpDataSource {
    override suspend fun sendEmailCode(email: String): NetworkResult<BaseResponse<Nothing>> =
        safeApiCall { onboardingApiService.sendEmailCode(email) }


    override suspend fun emailCodeCheck(emailCodeCheckRequest: EmailCodeCheckRequest): NetworkResult<BaseResponse<Nothing>> =
        safeApiCall { onboardingApiService.emailCodeCheck(emailCodeCheckRequest) }

    override suspend fun signUp(signUpRequest: SignUpRequest): NetworkResult<BaseResponse<Nothing>> =
        safeApiCall { onboardingApiService.signUp(signUpRequest) }

    override suspend fun kakaoLogIn(kakaoLoginRequest: KakaoLoginRequest): NetworkResult<BaseResponse<AuthTokens>> =
       safeApiCall { onboardingApiService.kakaoLogIn(kakaoLoginRequest) }


    override suspend fun findPassword(email: String): NetworkResult<BaseResponse<Nothing>> =
       safeApiCall { onboardingApiService.findPassword(email) }


    override suspend fun logIn(logInRequest: LogInRequest): NetworkResult<BaseResponse<AuthTokens>> =
       safeApiCall { onboardingApiService.logIn(logInRequest) }

}