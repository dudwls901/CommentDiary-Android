package com.movingmaker.data.remote.datasource

import com.movingmaker.data.remote.model.request.ChangePasswordRequest
import com.movingmaker.data.remote.model.request.EmailCodeCheckRequest
import com.movingmaker.data.remote.model.request.KakaoLoginRequest
import com.movingmaker.data.remote.model.request.KakaoSignUpRequest
import com.movingmaker.data.remote.model.request.LogInRequest
import com.movingmaker.data.remote.model.request.SignUpRequest
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.response.AuthTokens
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.Login
import com.movingmaker.domain.model.response.MyInfo

interface MemberRemoteDataSource {

    suspend fun sendEmailCode(email: String): NetworkResult<BaseResponse<String>>

    suspend fun checkEmailCode(emailCodeCheckRequest: EmailCodeCheckRequest): NetworkResult<BaseResponse<String>>

    suspend fun signUp(signUpRequest: SignUpRequest): NetworkResult<BaseResponse<String>>

    suspend fun logIn(logInRequest: LogInRequest): NetworkResult<BaseResponse<Login>>

    suspend fun kakaoLogIn(kakaoLoginRequest: KakaoLoginRequest): NetworkResult<BaseResponse<Login>>

    suspend fun kakaoSignUpSetAccepts(kakaoSignUpRequest: KakaoSignUpRequest): NetworkResult<BaseResponse<String>>

    suspend fun findPassword(email: String): NetworkResult<BaseResponse<String>>

    suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): NetworkResult<BaseResponse<String>>

    suspend fun patchCommentPushState(): NetworkResult<BaseResponse<Map<String, Boolean>>>

    suspend fun logOut(): NetworkResult<BaseResponse<String>>

    suspend fun signOut(): NetworkResult<BaseResponse<String>>

    suspend fun getMyPage(): NetworkResult<BaseResponse<MyInfo>>

    suspend fun reIssueToken(): NetworkResult<BaseResponse<AuthTokens>>

}