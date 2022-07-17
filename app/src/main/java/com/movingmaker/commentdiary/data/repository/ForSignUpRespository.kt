package com.movingmaker.commentdiary.data.repository

import com.movingmaker.commentdiary.data.remote.RetrofitClient
import com.movingmaker.commentdiary.data.remote.request.EmailCodeCheckRequest
import com.movingmaker.commentdiary.data.remote.request.KakaoLoginRequest
import com.movingmaker.commentdiary.data.remote.request.LogInRequest
import com.movingmaker.commentdiary.data.remote.request.SignUpRequest
import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.data.remote.response.LogInResponse
import retrofit2.Response

class ForSignUpRespository {

    companion object{
        val INSTANCE = ForSignUpRespository()
        val TAG = "레트로핏 로그"
    }


    suspend fun sendEmailCode(email: String): Response<IsSuccessResponse> =
        RetrofitClient.onboardingApiService.sendEmailCode(email)


    suspend fun emailCodeCheck(emailCodeCheckRequest: EmailCodeCheckRequest): Response<IsSuccessResponse> =
        RetrofitClient.onboardingApiService.emailCodeCheck(emailCodeCheckRequest)


    suspend fun signUp(signUpRequest: SignUpRequest): Response<IsSuccessResponse> =
        RetrofitClient.onboardingApiService.signUp(signUpRequest)

    suspend fun logIn(logInRequest: LogInRequest): Response<LogInResponse> =
        RetrofitClient.onboardingApiService.logIn(logInRequest)

    suspend fun kakaoLogin(kakaoLoginRequest: KakaoLoginRequest) : Response<LogInResponse> = RetrofitClient.onboardingApiService.kakaoLogIn(kakaoLoginRequest)

    suspend fun findPassword(email: String): Response<IsSuccessResponse> =
        RetrofitClient.onboardingApiService.findPassword(email)

    suspend fun signOut(): Response<IsSuccessResponse> {
        return RetrofitClient.myPageApiService.signOut()
    }
}