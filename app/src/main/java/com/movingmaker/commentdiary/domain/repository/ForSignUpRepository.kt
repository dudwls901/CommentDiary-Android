package com.movingmaker.commentdiary.domain.repository

import com.movingmaker.commentdiary.data.model.AuthTokens
import com.movingmaker.commentdiary.data.remote.request.*
import com.movingmaker.commentdiary.domain.model.BaseResponse
import com.movingmaker.commentdiary.domain.model.NetworkResult

interface ForSignUpRepository {

    suspend fun sendEmailCode(email: String): NetworkResult<BaseResponse<String>>

    suspend fun emailCodeCheck(emailCodeCheckRequest: EmailCodeCheckRequest): NetworkResult<BaseResponse<String>>

    suspend fun signUp(signUpRequest: SignUpRequest): NetworkResult<BaseResponse<String>>

    suspend fun logIn(logInRequest: LogInRequest): NetworkResult<BaseResponse<AuthTokens>>

    suspend fun kakaoLogIn(kakaoLoginRequest: KakaoLoginRequest): NetworkResult<BaseResponse<AuthTokens>>

    suspend fun findPassword(email: String): NetworkResult<BaseResponse<String>>

    suspend fun kakaoSignUpSetAccepts(kakaoSignUpRequest: KakaoSignUpRequest): NetworkResult<BaseResponse<String>>
}