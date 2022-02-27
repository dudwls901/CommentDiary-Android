package com.movingmaker.commentdiary.model.remote.api

import com.movingmaker.commentdiary.model.remote.Url
import com.movingmaker.commentdiary.model.remote.request.EmailCodeCheckRequest
import com.movingmaker.commentdiary.model.remote.request.SignUpRequest
import com.movingmaker.commentdiary.model.remote.response.EmailCodeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface OnboardingApiService {
//    @GET(Url.SEND_EMAIL)
//    fun sendEmail(@Query("email") email: String) : Call<SendEmailCodeResponse>

    @GET(Url.SEND_EMAIL_CODE)
    suspend fun sendEmailCode(@Query("email") email: String) : Response<EmailCodeResponse>

    @POST(Url.EMAIL_CODE_CHECK)
    suspend fun emailCodeCheck(@Body emailCodeCheckRequest: EmailCodeCheckRequest): Response<EmailCodeResponse>

    @POST(Url.SIGN_UP)
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<EmailCodeResponse>
}