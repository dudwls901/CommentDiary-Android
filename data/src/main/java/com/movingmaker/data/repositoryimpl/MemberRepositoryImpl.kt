package com.movingmaker.data.repositoryimpl

import com.movingmaker.data.remote.datasource.MemberRemoteDataSource
import com.movingmaker.data.remote.model.request.toDataModel
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.request.ChangePasswordModel
import com.movingmaker.domain.model.request.EmailCodeCheckModel
import com.movingmaker.domain.model.request.KakaoLoginModel
import com.movingmaker.domain.model.request.KakaoSignUpModel
import com.movingmaker.domain.model.request.LogInModel
import com.movingmaker.domain.model.request.SignUpModel
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.Login
import com.movingmaker.domain.repository.MemberRepository
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(
    private val memberRemoteDataSource: MemberRemoteDataSource
) : MemberRepository {
    override suspend fun sendEmailCode(email: String): NetworkResult<BaseResponse<String>> =
        memberRemoteDataSource.sendEmailCode(email)

    override suspend fun checkEmailCode(emailCodeCheckModel: EmailCodeCheckModel): NetworkResult<BaseResponse<String>> =
        memberRemoteDataSource.checkEmailCode(emailCodeCheckModel.toDataModel())

    override suspend fun signUp(signUpModel: SignUpModel): NetworkResult<BaseResponse<String>> =
        memberRemoteDataSource.signUp(signUpModel.toDataModel())

    override suspend fun logIn(logInModel: LogInModel): NetworkResult<BaseResponse<Login>> =
        memberRemoteDataSource.logIn(logInModel.toDataModel())


    override suspend fun kakaoLogIn(kakaoLoginModel: KakaoLoginModel): NetworkResult<BaseResponse<Login>> =
        memberRemoteDataSource.kakaoLogIn(kakaoLoginModel.toDataModel())


    override suspend fun findPassword(email: String): NetworkResult<BaseResponse<String>> =
        memberRemoteDataSource.findPassword(email)

    override suspend fun changePassword(changePasswordModel: ChangePasswordModel): NetworkResult<BaseResponse<String>> =
        memberRemoteDataSource.changePassword(changePasswordModel.toDataModel())

    override suspend fun kakaoSignUpSetAccepts(kakaoSignUpModel: KakaoSignUpModel): NetworkResult<BaseResponse<String>> =
        memberRemoteDataSource.kakaoSignUpSetAccepts(kakaoSignUpModel.toDataModel())
}