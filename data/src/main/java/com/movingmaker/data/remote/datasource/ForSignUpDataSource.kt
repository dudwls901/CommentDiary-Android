package com.movingmaker.data.remote.datasource

import com.movingmaker.data.remote.model.request.EmailCodeCheckRequest
import com.movingmaker.data.remote.model.request.KakaoLoginRequest
import com.movingmaker.data.remote.model.request.KakaoSignUpRequest
import com.movingmaker.data.remote.model.request.LogInRequest
import com.movingmaker.data.remote.model.request.SignUpRequest
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.response.AuthTokens
import com.movingmaker.domain.model.response.BaseResponse

interface ForSignUpDataSource {

    suspend fun sendEmailCode(email: String): NetworkResult<BaseResponse<String>>

    suspend fun emailCodeCheck(emailCodeCheckRequest: EmailCodeCheckRequest): NetworkResult<BaseResponse<String>>

    suspend fun signUp(signUpRequest: SignUpRequest): NetworkResult<BaseResponse<String>>

    suspend fun logIn(logInRequest: LogInRequest): NetworkResult<BaseResponse<AuthTokens>>

    suspend fun kakaoLogIn(kakaoLoginRequest: KakaoLoginRequest): NetworkResult<BaseResponse<AuthTokens>>

    suspend fun findPassword(email: String): NetworkResult<BaseResponse<String>>

    suspend fun kakaoSignUpSetAccepts(kakaoSignUpRequest: KakaoSignUpRequest): NetworkResult<BaseResponse<String>>

}