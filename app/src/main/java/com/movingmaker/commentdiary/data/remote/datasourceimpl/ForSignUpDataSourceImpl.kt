package com.movingmaker.commentdiary.data.remote.datasourceimpl

import com.movingmaker.commentdiary.data.remote.api.OnboardingApiService
import com.movingmaker.commentdiary.data.remote.datasource.ForSignUpDataSource
import com.movingmaker.commentdiary.data.remote.request.EmailCodeCheckRequest
import com.movingmaker.commentdiary.data.remote.request.KakaoLoginRequest
import com.movingmaker.commentdiary.data.remote.request.LogInRequest
import com.movingmaker.commentdiary.data.remote.request.SignUpRequest
import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.data.remote.response.LogInResponse
import retrofit2.Response
import javax.inject.Inject

class ForSignUpDataSourceImpl @Inject constructor(
    private val onboardingApiService: OnboardingApiService
) : ForSignUpDataSource {

    override suspend fun sendEmailCode(email: String): Response<IsSuccessResponse> =
        onboardingApiService.sendEmailCode(email)

    override suspend fun emailCodeCheck(emailCodeCheckRequest: EmailCodeCheckRequest): Response<IsSuccessResponse> =
        onboardingApiService.emailCodeCheck(emailCodeCheckRequest)

    override suspend fun signUp(signUpRequest: SignUpRequest): Response<IsSuccessResponse> =
        onboardingApiService.signUp(signUpRequest)

    override suspend fun logIn(logInRequest: LogInRequest): Response<LogInResponse> =
        onboardingApiService.logIn(logInRequest)

    override suspend fun kakaoLogIn(kakaoLoginRequest: KakaoLoginRequest): Response<LogInResponse> =
        onboardingApiService.kakaoLogIn(kakaoLoginRequest)

    override suspend fun findPassword(email: String): Response<IsSuccessResponse> =
        onboardingApiService.findPassword(email)
}