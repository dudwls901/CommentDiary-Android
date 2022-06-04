package com.movingmaker.commentdiary.model.remote.api

import com.movingmaker.commentdiary.model.remote.request.EmailCodeCheckRequest
import com.movingmaker.commentdiary.model.remote.request.LogInRequest
import com.movingmaker.commentdiary.model.remote.request.SignUpRequest
import com.movingmaker.commentdiary.model.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.model.remote.response.LogInResponse
import com.movingmaker.commentdiary.util.Url.EMAIL_CODE_CHECK
import com.movingmaker.commentdiary.util.Url.FIND_PW
import com.movingmaker.commentdiary.util.Url.LOG_IN
import com.movingmaker.commentdiary.util.Url.SEND_EMAIL_CODE
import com.movingmaker.commentdiary.util.Url.SIGNUP
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface OnboardingApiService {
//    @GET(Url.SEND_EMAIL)
//    fun sendEmail(@Query("email") email: String) : Call<SendEmailCodeResponse>

    @GET(SEND_EMAIL_CODE)
    suspend fun sendEmailCode(@Query("email") email: String) : Response<IsSuccessResponse>

    @POST(EMAIL_CODE_CHECK)
    suspend fun emailCodeCheck(@Body emailCodeCheckRequest: EmailCodeCheckRequest): Response<IsSuccessResponse>

    @POST(SIGNUP)
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<IsSuccessResponse>

    @POST(LOG_IN)
    suspend fun logIn(@Body logInRequest: LogInRequest): Response<LogInResponse>

    @GET(FIND_PW)
    suspend fun findPassword(@Query("email") email: String) : Response<IsSuccessResponse>
}