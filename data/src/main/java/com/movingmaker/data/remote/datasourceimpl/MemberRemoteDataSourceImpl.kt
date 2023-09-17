package com.movingmaker.data.remote.datasourceimpl

import com.movingmaker.data.remote.api.BearerAndXAuthTokenApiService
import com.movingmaker.data.remote.api.BearerApiService
import com.movingmaker.data.remote.api.NoHeaderApiService
import com.movingmaker.data.remote.api.XAuthTokenAndRefreshTokenApiService
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
import com.movingmaker.domain.model.response.Login
import com.movingmaker.domain.model.response.MyInfo
import javax.inject.Inject

class MemberRemoteDataSourceImpl @Inject constructor(
    private val noHeaderApiService: NoHeaderApiService,
    private val bearerApiService: BearerApiService,
    private val bearerAndXAuthTokenApiService: BearerAndXAuthTokenApiService,
    private val xAuthTokenAndRefreshTokenApiService: XAuthTokenAndRefreshTokenApiService
) : MemberRemoteDataSource {
    override suspend fun sendEmailCode(email: String): NetworkResult<BaseResponse<String>> =
        safeApiCall { noHeaderApiService.sendEmailCode(email) }

    override suspend fun checkEmailCode(emailCodeCheckRequest: EmailCodeCheckRequest): NetworkResult<BaseResponse<String>> =
        safeApiCall { noHeaderApiService.checkEmailCode(emailCodeCheckRequest) }

    override suspend fun signUp(signUpRequest: SignUpRequest): NetworkResult<BaseResponse<String>> =
        safeApiCall { noHeaderApiService.signUp(signUpRequest) }

    override suspend fun kakaoLogIn(kakaoLoginRequest: KakaoLoginRequest): NetworkResult<BaseResponse<Login>> =
        safeApiCall { noHeaderApiService.kakaoLogIn(kakaoLoginRequest) }

    override suspend fun findPassword(email: String): NetworkResult<BaseResponse<String>> =
        safeApiCall { noHeaderApiService.findPassword(email) }

    override suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): NetworkResult<BaseResponse<String>> =
        safeApiCall { bearerApiService.changePassword(changePasswordRequest) }

    override suspend fun logIn(logInRequest: LogInRequest): NetworkResult<BaseResponse<Login>> =
        safeApiCall { noHeaderApiService.logIn(logInRequest) }

    override suspend fun kakaoSignUpSetAccepts(kakaoSignUpRequest: KakaoSignUpRequest): NetworkResult<BaseResponse<String>> =
        safeApiCall { noHeaderApiService.kakaoSignUpSetAccepts(kakaoSignUpRequest) }

    override suspend fun signOut(): NetworkResult<BaseResponse<String>> =
        safeApiCall { bearerApiService.signOut() }

    override suspend fun getMyPage(): NetworkResult<BaseResponse<MyInfo>> =
        safeApiCall { bearerApiService.getMyPage() }

    override suspend fun patchCommentPushState(): NetworkResult<BaseResponse<Map<String, Boolean>>> =
        safeApiCall { bearerApiService.patchCommentPushState() }

    override suspend fun logOut(): NetworkResult<BaseResponse<String>> =
        safeApiCall { bearerAndXAuthTokenApiService.logOut() }

    override suspend fun reIssueToken(): NetworkResult<BaseResponse<AuthTokens>> =
        safeApiCall { xAuthTokenAndRefreshTokenApiService.reIssueToken() }
}