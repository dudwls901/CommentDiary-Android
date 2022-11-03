package com.movingmaker.commentdiary.data.repositoryimpl

import com.movingmaker.commentdiary.data.model.AuthTokens
import com.movingmaker.commentdiary.data.remote.datasource.ForSignUpDataSource
import com.movingmaker.commentdiary.data.remote.request.*
import com.movingmaker.commentdiary.domain.model.BaseResponse
import com.movingmaker.commentdiary.domain.model.NetworkResult
import com.movingmaker.commentdiary.domain.repository.ForSignUpRepository
import javax.inject.Inject

class ForSignUpRepositoryImpl @Inject constructor(
    private val signUpDataSource: ForSignUpDataSource
) : ForSignUpRepository {
    override suspend fun sendEmailCode(email: String): NetworkResult<BaseResponse<String>> =
        signUpDataSource.sendEmailCode(email)


    override suspend fun emailCodeCheck(emailCodeCheckRequest: EmailCodeCheckRequest): NetworkResult<BaseResponse<String>> =
        signUpDataSource.emailCodeCheck(emailCodeCheckRequest)


    override suspend fun signUp(signUpRequest: SignUpRequest): NetworkResult<BaseResponse<String>> =
        signUpDataSource.signUp(signUpRequest)


    override suspend fun logIn(logInRequest: LogInRequest): NetworkResult<BaseResponse<AuthTokens>> =
        signUpDataSource.logIn(logInRequest)


    override suspend fun kakaoLogIn(kakaoLoginRequest: KakaoLoginRequest): NetworkResult<BaseResponse<AuthTokens>> =
        signUpDataSource.kakaoLogIn(kakaoLoginRequest)


    override suspend fun findPassword(email: String): NetworkResult<BaseResponse<String>> =
        signUpDataSource.findPassword(email)

    override suspend fun kakaoSignUpSetAccepts(kakaoSignUpRequest: KakaoSignUpRequest): NetworkResult<BaseResponse<String>> =
        signUpDataSource.kakaoSignUpSetAccepts(kakaoSignUpRequest)
}