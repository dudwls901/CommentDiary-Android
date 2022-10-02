package com.movingmaker.commentdiary.data.repositoryimpl

import com.movingmaker.commentdiary.data.remote.datasource.ForSignUpDataSource
import com.movingmaker.commentdiary.data.remote.request.EmailCodeCheckRequest
import com.movingmaker.commentdiary.data.remote.request.KakaoLoginRequest
import com.movingmaker.commentdiary.data.remote.request.LogInRequest
import com.movingmaker.commentdiary.data.remote.request.SignUpRequest
import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.data.remote.response.LogInResponse
import com.movingmaker.commentdiary.domain.repository.ForSignUpRepository
import retrofit2.Response
import javax.inject.Inject

class ForSignUpRepositoryImpl @Inject constructor(
    private val signUpDataSource: ForSignUpDataSource
) : ForSignUpRepository {

    override suspend fun sendEmailCode(email: String): Response<IsSuccessResponse> =
        signUpDataSource.sendEmailCode(email)


    override suspend fun emailCodeCheck(emailCodeCheckRequest: EmailCodeCheckRequest): Response<IsSuccessResponse> =
        signUpDataSource.emailCodeCheck(emailCodeCheckRequest)


    override suspend fun signUp(signUpRequest: SignUpRequest): Response<IsSuccessResponse> =
        signUpDataSource.signUp(signUpRequest)

    override suspend fun logIn(logInRequest: LogInRequest): Response<LogInResponse> =
        signUpDataSource.logIn(logInRequest)

    override suspend fun kakaoLogIn(kakaoLoginRequest: KakaoLoginRequest): Response<LogInResponse> =
        signUpDataSource.kakaoLogIn(kakaoLoginRequest)

    override suspend fun findPassword(email: String): Response<IsSuccessResponse> =
        signUpDataSource.findPassword(email)
}