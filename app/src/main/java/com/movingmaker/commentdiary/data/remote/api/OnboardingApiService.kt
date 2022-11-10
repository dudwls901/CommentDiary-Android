package com.movingmaker.commentdiary.data.remote.api

import com.movingmaker.commentdiary.data.model.AuthTokens
import com.movingmaker.commentdiary.data.remote.request.EmailCodeCheckRequest
import com.movingmaker.commentdiary.data.remote.request.KakaoLoginRequest
import com.movingmaker.commentdiary.data.remote.request.KakaoSignUpRequest
import com.movingmaker.commentdiary.data.remote.request.LogInRequest
import com.movingmaker.commentdiary.data.remote.request.SignUpRequest
import com.movingmaker.commentdiary.data.util.AUTH_LOG_IN
import com.movingmaker.commentdiary.data.util.AUTH_SIGNUP
import com.movingmaker.commentdiary.data.util.EMAIL_CODE_CHECK
import com.movingmaker.commentdiary.data.util.FIND_PW
import com.movingmaker.commentdiary.data.util.LOG_IN
import com.movingmaker.commentdiary.data.util.MEMBERS
import com.movingmaker.commentdiary.data.util.SEND_EMAIL_CODE
import com.movingmaker.commentdiary.data.util.SIGNUP
import com.movingmaker.commentdiary.domain.model.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface OnboardingApiService {

    @GET(SEND_EMAIL_CODE)
    suspend fun sendEmailCode(@Query("email") email: String): Response<BaseResponse<String>>

    @POST(EMAIL_CODE_CHECK)
    suspend fun emailCodeCheck(@Body emailCodeCheckRequest: EmailCodeCheckRequest): Response<BaseResponse<String>>

    @POST(SIGNUP)
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<BaseResponse<String>>

    @POST(LOG_IN)
    suspend fun logIn(@Body logInRequest: LogInRequest): Response<BaseResponse<AuthTokens>>

    @POST(AUTH_LOG_IN)
    suspend fun kakaoLogIn(@Body kakaoLoginRequest: KakaoLoginRequest): Response<BaseResponse<AuthTokens>>

    @GET(FIND_PW)
    suspend fun findPassword(@Query("email") email: String): Response<BaseResponse<String>>

    @POST(MEMBERS + AUTH_SIGNUP)
    suspend fun kakaoSignUpSetAccepts(@Body kakaoSignUpRequest: KakaoSignUpRequest): Response<BaseResponse<String>>
}