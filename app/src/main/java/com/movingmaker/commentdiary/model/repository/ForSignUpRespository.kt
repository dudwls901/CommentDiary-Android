package com.movingmaker.commentdiary.model.repository

import com.movingmaker.commentdiary.model.remote.RetrofitClient
import com.movingmaker.commentdiary.model.remote.request.EmailCodeCheckRequest
import com.movingmaker.commentdiary.model.remote.request.LogInRequest
import com.movingmaker.commentdiary.model.remote.request.SignUpRequest
import com.movingmaker.commentdiary.model.remote.response.EmailCodeResponse
import com.movingmaker.commentdiary.model.remote.response.LogInResponse
import retrofit2.Response

class ForSignUpRespository {

    companion object{
        val INSTANCE = ForSignUpRespository()
        val TAG = "레트로핏 로그"
    }


    suspend fun sendEmailCode(email: String): Response<EmailCodeResponse> =
        RetrofitClient.onboardingApiService.sendEmailCode(email)


    suspend fun emailCodeCheck(emailCodeCheckRequest: EmailCodeCheckRequest): Response<EmailCodeResponse> =
        RetrofitClient.onboardingApiService.emailCodeCheck(emailCodeCheckRequest)


    suspend fun signUp(signUpRequest: SignUpRequest): Response<EmailCodeResponse> =
        RetrofitClient.onboardingApiService.signUp(signUpRequest)

    suspend fun logIn(logInRequest: LogInRequest): Response<LogInResponse> =
        RetrofitClient.onboardingApiService.logIn(logInRequest)

    suspend fun findPassword(email: String): Response<EmailCodeResponse> =
        RetrofitClient.onboardingApiService.findPassword(email)


//    private val onboardingApiService: OnboardingApiService = RetrofitClient.onboardingApiService

//suspend fun sendEmail(email: String){
//
//        val call = onboardingApiService.sendEmail(email = email).let{
//            it
//        }?: return
//
//
//        call.enqueue(object: retrofit2.Callback<SendEmailCodeResponse>{
//            override fun onResponse(
//                call: Call<SendEmailCodeResponse>,
//                response: Response<SendEmailCodeResponse>
//            ) {
//                Log.d(TAG, "onResponse(): called / response : ${response.raw()}")
//            }
//
//            override fun onFailure(call: Call<SendEmailCodeResponse>, t: Throwable) {
//                Log.d(TAG, "RetrofitManager - onFailure(): called /t : $t")
//            }
//        })
//
//    }

}