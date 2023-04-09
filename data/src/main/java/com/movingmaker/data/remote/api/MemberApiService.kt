package com.movingmaker.data.remote.api

import com.movingmaker.data.remote.model.request.ChangePasswordRequest
import com.movingmaker.data.remote.model.request.EmailCodeCheckRequest
import com.movingmaker.data.remote.model.request.KakaoLoginRequest
import com.movingmaker.data.remote.model.request.KakaoSignUpRequest
import com.movingmaker.data.remote.model.request.LogInRequest
import com.movingmaker.data.remote.model.request.SignUpRequest
import com.movingmaker.data.remote.model.response.AuthTokensResponse
import com.movingmaker.data.remote.model.response.BaseResponse
import com.movingmaker.data.util.AUTH_LOG_IN
import com.movingmaker.data.util.AUTH_SIGNUP
import com.movingmaker.data.util.EMAIL_CODE_CHECK
import com.movingmaker.data.util.FIND_PW
import com.movingmaker.data.util.LOG_IN
import com.movingmaker.data.util.MEMBERS
import com.movingmaker.data.util.SEND_EMAIL_CODE
import com.movingmaker.data.util.SIGNUP
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface MemberApiService {

    @GET(SEND_EMAIL_CODE)
    suspend fun sendEmailCode(@Query("email") email: String): Response<BaseResponse<String>>

    @POST(EMAIL_CODE_CHECK)
    suspend fun checkEmailCode(@Body emailCodeCheckRequest: EmailCodeCheckRequest): Response<BaseResponse<String>>

    @POST(SIGNUP)
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<BaseResponse<String>>

    @POST(LOG_IN)
    suspend fun logIn(@Body logInRequest: LogInRequest): Response<BaseResponse<AuthTokensResponse>>

    @POST(AUTH_LOG_IN)
    suspend fun kakaoLogIn(@Body kakaoLoginRequest: KakaoLoginRequest): Response<BaseResponse<AuthTokensResponse>>

    //todo bearear 필요 없는지 확인
    @POST(MEMBERS + AUTH_SIGNUP)
    suspend fun kakaoSignUpSetAccepts(@Body kakaoSignUpRequest: KakaoSignUpRequest): Response<BaseResponse<String>>

    @PATCH(MEMBERS)
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): Response<BaseResponse<String>>

    @GET(FIND_PW)
    suspend fun findPassword(@Query("email") email: String): Response<BaseResponse<String>>

}