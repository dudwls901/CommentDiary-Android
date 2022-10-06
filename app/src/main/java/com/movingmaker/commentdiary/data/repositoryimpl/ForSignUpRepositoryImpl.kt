package com.movingmaker.commentdiary.data.repositoryimpl

import com.movingmaker.commentdiary.data.model.AuthTokens
import com.movingmaker.commentdiary.data.remote.datasource.ForSignUpDataSource
import com.movingmaker.commentdiary.data.remote.request.EmailCodeCheckRequest
import com.movingmaker.commentdiary.data.remote.request.KakaoLoginRequest
import com.movingmaker.commentdiary.data.remote.request.LogInRequest
import com.movingmaker.commentdiary.data.remote.request.SignUpRequest
import com.movingmaker.commentdiary.domain.model.BaseResponse
import com.movingmaker.commentdiary.domain.model.NetworkResult
import com.movingmaker.commentdiary.domain.repository.ForSignUpRepository
import javax.inject.Inject

class ForSignUpRepositoryImpl @Inject constructor(
    private val signUpDataSource: ForSignUpDataSource
) : ForSignUpRepository {
    override suspend fun sendEmailCode(email: String): NetworkResult<BaseResponse<Nothing>> =
        signUpDataSource.sendEmailCode(email)


    override suspend fun emailCodeCheck(emailCodeCheckRequest: EmailCodeCheckRequest): NetworkResult<BaseResponse<Nothing>> =
        signUpDataSource.emailCodeCheck(emailCodeCheckRequest)


    override suspend fun signUp(signUpRequest: SignUpRequest): NetworkResult<BaseResponse<Nothing>> =
        signUpDataSource.signUp(signUpRequest)


    override suspend fun logIn(logInRequest: LogInRequest): NetworkResult<BaseResponse<AuthTokens>> =
        signUpDataSource.logIn(logInRequest)


    override suspend fun kakaoLogIn(kakaoLoginRequest: KakaoLoginRequest): NetworkResult<BaseResponse<AuthTokens>> =
        signUpDataSource.kakaoLogIn(kakaoLoginRequest)


    override suspend fun findPassword(email: String): NetworkResult<BaseResponse<Nothing>> =
        signUpDataSource.findPassword(email)

}