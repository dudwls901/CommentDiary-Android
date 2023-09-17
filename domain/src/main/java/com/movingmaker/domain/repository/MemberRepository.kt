package com.movingmaker.domain.repository

import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.request.ChangePasswordModel
import com.movingmaker.domain.model.request.EmailCodeCheckModel
import com.movingmaker.domain.model.request.KakaoLoginModel
import com.movingmaker.domain.model.request.KakaoSignUpModel
import com.movingmaker.domain.model.request.LogInModel
import com.movingmaker.domain.model.request.SignUpModel
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.Login
import com.movingmaker.domain.model.response.MyInfo

interface MemberRepository {

    suspend fun sendEmailCode(email: String): NetworkResult<BaseResponse<String>>

    suspend fun checkEmailCode(emailCodeCheckModel: EmailCodeCheckModel): NetworkResult<BaseResponse<String>>

    suspend fun signUp(signUpModel: SignUpModel): NetworkResult<BaseResponse<String>>

    suspend fun logIn(logInModel: LogInModel): NetworkResult<BaseResponse<Login>>

    suspend fun kakaoLogIn(kakaoLoginModel: KakaoLoginModel): NetworkResult<BaseResponse<Login>>

    suspend fun kakaoSignUpSetAccepts(kakaoSignUpModel: KakaoSignUpModel): NetworkResult<BaseResponse<String>>

    suspend fun findPassword(email: String): NetworkResult<BaseResponse<String>>

    suspend fun changePassword(changePasswordModel: ChangePasswordModel): NetworkResult<BaseResponse<String>>

    suspend fun patchCommentPushState(): NetworkResult<BaseResponse<Map<String, Boolean>>>

    suspend fun logOut(): NetworkResult<BaseResponse<String>>

    suspend fun signOut(): NetworkResult<BaseResponse<String>>

    suspend fun getMyPage(): NetworkResult<BaseResponse<MyInfo>>
}