package com.movingmaker.commentdiary.data.remote.datasource

import com.movingmaker.commentdiary.data.model.AuthTokens
import com.movingmaker.commentdiary.data.remote.request.EmailCodeCheckRequest
import com.movingmaker.commentdiary.data.remote.request.KakaoLoginRequest
import com.movingmaker.commentdiary.data.remote.request.LogInRequest
import com.movingmaker.commentdiary.data.remote.request.SignUpRequest
import com.movingmaker.commentdiary.domain.model.BaseResponse
import com.movingmaker.commentdiary.domain.model.NetworkResult

interface ForSignUpDataSource {

    suspend fun sendEmailCode(email: String): NetworkResult<BaseResponse<Nothing>>

    suspend fun emailCodeCheck(emailCodeCheckRequest: EmailCodeCheckRequest): NetworkResult<BaseResponse<Nothing>>

    suspend fun signUp(signUpRequest: SignUpRequest): NetworkResult<BaseResponse<Nothing>>

    suspend fun logIn(logInRequest: LogInRequest): NetworkResult<BaseResponse<AuthTokens>>

    suspend fun kakaoLogIn(kakaoLoginRequest: KakaoLoginRequest): NetworkResult<BaseResponse<AuthTokens>>

    suspend fun findPassword(email: String): NetworkResult<BaseResponse<Nothing>>

}