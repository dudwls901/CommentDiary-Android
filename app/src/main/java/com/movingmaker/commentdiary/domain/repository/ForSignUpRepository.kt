package com.movingmaker.commentdiary.domain.repository

import com.movingmaker.commentdiary.data.remote.request.EmailCodeCheckRequest
import com.movingmaker.commentdiary.data.remote.request.KakaoLoginRequest
import com.movingmaker.commentdiary.data.remote.request.LogInRequest
import com.movingmaker.commentdiary.data.remote.request.SignUpRequest
import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.data.remote.response.LogInResponse
import retrofit2.Response

interface ForSignUpRepository {

    suspend fun sendEmailCode(email: String): Response<IsSuccessResponse>

    suspend fun emailCodeCheck(emailCodeCheckRequest: EmailCodeCheckRequest): Response<IsSuccessResponse>

    suspend fun signUp(signUpRequest: SignUpRequest): Response<IsSuccessResponse>

    suspend fun logIn(logInRequest: LogInRequest): Response<LogInResponse>

    suspend fun kakaoLogIn(kakaoLoginRequest: KakaoLoginRequest): Response<LogInResponse>

    suspend fun findPassword(email: String): Response<IsSuccessResponse>
}