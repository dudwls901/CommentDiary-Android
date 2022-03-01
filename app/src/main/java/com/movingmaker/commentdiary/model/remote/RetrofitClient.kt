package com.movingmaker.commentdiary.model.remote

import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.movingmaker.commentdiary.BuildConfig
import com.movingmaker.commentdiary.CodaApplication
import com.movingmaker.commentdiary.model.remote.api.MyPageApiService
import com.movingmaker.commentdiary.model.remote.api.OnboardingApiService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

object RetrofitClient {
    val onboardingApiService: OnboardingApiService by lazy {
        getOnboardingRetrofit().create(OnboardingApiService::class.java)
    }

    val myPageApiService: MyPageApiService by lazy {
        getMyPageRetrofit().create(MyPageApiService::class.java)
    }

    private fun getOnboardingRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Url.CODA_BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create()
                )
            )
            .client(buildOkHttpClient())
            .build()
    }

    private fun getMyPageRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Url.BASE_URL)
            .client(buildHeaderOkHttpClient())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create()
                )
            )
            .build()
    }
    //로깅 + 헤더
    private fun buildHeaderOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        //todo Authenticator로 바꾸기
        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
//            .authenticator(TokenAuthenticator())
            .addInterceptor(HeaderInterceptor())
            .build()
    }


    private fun buildOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
//            .addInterceptor(HeaderInterceptor("abc"))
            .build()
    }

    class TokenAuthenticator : Authenticator {
        override fun authenticate(route: Route?, response: Response): Request? {
//            Log.d("111111111111", "Token " + "token")
            if (response.request.header("Authorization") != null) {
//                Log.d("2222222222222", "Token " + "token")
                return null
            }

            return response.request.newBuilder().header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1IiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTY0NjE0NDI5Nn0.24CJEkIR4MpPaTjMOCO8iUjozRV3KkVpkNsmn6odndt-rshS-IMgE8OacbqRL3aVLubgn8OqRySztwH6v_N3Yw").build()
        }
    }
//    OkHttpClient.Builder().authenticator(TokenAuthenticator()).build()

    class HeaderInterceptor: Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {


            val accessTokenExpiresIn  = runBlocking {
                CodaApplication.getInstance().getDataStore().accessTokenExpiresIn.first()
            }
            var accessToken = ""
            if(accessTokenExpiresIn <= System.currentTimeMillis()){
                //토큰 갱신 api 호출
                Log.d("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "intercept: 토큰 만료됨 ")
            }
            else{
                accessToken = runBlocking {
                    CodaApplication.getInstance().getDataStore().accessToken.first()
                }
            }

            val token = "Bearer $accessToken"
            Log.d("3333333333333",  token)
            val newRequest = chain.request().newBuilder().addHeader("Authorization", token)
                .build()
            return chain.proceed(newRequest)
        }
    }

}