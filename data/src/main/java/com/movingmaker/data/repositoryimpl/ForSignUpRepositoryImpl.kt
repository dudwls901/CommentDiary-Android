package com.movingmaker.data.repositoryimpl

import com.movingmaker.data.remote.datasource.ForSignUpDataSource
import com.movingmaker.data.remote.model.request.toDataModel
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.request.EmailCodeCheckModel
import com.movingmaker.domain.model.request.KakaoLoginModel
import com.movingmaker.domain.model.request.KakaoSignUpModel
import com.movingmaker.domain.model.request.LogInModel
import com.movingmaker.domain.model.request.SignUpModel
import com.movingmaker.domain.model.response.AuthTokens
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.repository.ForSignUpRepository
import javax.inject.Inject

class ForSignUpRepositoryImpl @Inject constructor(
    private val signUpDataSource: ForSignUpDataSource
) : ForSignUpRepository {
    override suspend fun sendEmailCode(email: String): NetworkResult<BaseResponse<String>> =
        signUpDataSource.sendEmailCode(email)


    override suspend fun emailCodeCheck(emailCodeCheckModel: EmailCodeCheckModel): NetworkResult<BaseResponse<String>> =
        signUpDataSource.emailCodeCheck(emailCodeCheckModel.toDataModel())


    override suspend fun signUp(signUpModel: SignUpModel): NetworkResult<BaseResponse<String>> =
        signUpDataSource.signUp(signUpModel.toDataModel())


    override suspend fun logIn(logInModel: LogInModel): NetworkResult<BaseResponse<AuthTokens>> =
        signUpDataSource.logIn(logInModel.toDataModel())


    override suspend fun kakaoLogIn(kakaoLoginModel: KakaoLoginModel): NetworkResult<BaseResponse<AuthTokens>> =
        signUpDataSource.kakaoLogIn(kakaoLoginModel.toDataModel())


    override suspend fun findPassword(email: String): NetworkResult<BaseResponse<String>> =
        signUpDataSource.findPassword(email)

    override suspend fun kakaoSignUpSetAccepts(kakaoSignUpModel: KakaoSignUpModel): NetworkResult<BaseResponse<String>> =
        signUpDataSource.kakaoSignUpSetAccepts(kakaoSignUpModel.toDataModel())
}