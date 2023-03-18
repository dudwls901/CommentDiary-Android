package com.movingmaker.data.remote.datasourceimpl

import com.movingmaker.data.remote.api.MemberApiService
import com.movingmaker.data.remote.datasource.MemberRemoteDataSource
import com.movingmaker.data.remote.model.request.ChangePasswordRequest
import com.movingmaker.data.remote.model.request.EmailCodeCheckRequest
import com.movingmaker.data.remote.model.request.KakaoLoginRequest
import com.movingmaker.data.remote.model.request.KakaoSignUpRequest
import com.movingmaker.data.remote.model.request.LogInRequest
import com.movingmaker.data.remote.model.request.SignUpRequest
import com.movingmaker.data.util.safeApiCall
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.response.AuthTokens
import com.movingmaker.domain.model.response.BaseResponse
import javax.inject.Inject

class MemberRemoteDataSourceImpl @Inject constructor(
    private val memberApiService: MemberApiService
) : MemberRemoteDataSource {
    override suspend fun sendEmailCode(email: String): NetworkResult<BaseResponse<String>> =
        safeApiCall { memberApiService.sendEmailCode(email) }

    override suspend fun checkEmailCode(emailCodeCheckRequest: EmailCodeCheckRequest): NetworkResult<BaseResponse<String>> =
        safeApiCall { memberApiService.checkEmailCode(emailCodeCheckRequest) }

    override suspend fun signUp(signUpRequest: SignUpRequest): NetworkResult<BaseResponse<String>> =
        safeApiCall { memberApiService.signUp(signUpRequest) }

    override suspend fun kakaoLogIn(kakaoLoginRequest: KakaoLoginRequest): NetworkResult<BaseResponse<AuthTokens>> =
        safeApiCall { memberApiService.kakaoLogIn(kakaoLoginRequest) }

    override suspend fun findPassword(email: String): NetworkResult<BaseResponse<String>> =
        safeApiCall { memberApiService.findPassword(email) }

    override suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): NetworkResult<BaseResponse<String>> =
        safeApiCall { memberApiService.changePassword(changePasswordRequest) }

    override suspend fun logIn(logInRequest: LogInRequest): NetworkResult<BaseResponse<AuthTokens>> =
        safeApiCall { memberApiService.logIn(logInRequest) }

    override suspend fun kakaoSignUpSetAccepts(kakaoSignUpRequest: KakaoSignUpRequest): NetworkResult<BaseResponse<String>> =
        safeApiCall { memberApiService.kakaoSignUpSetAccepts(kakaoSignUpRequest) }
}