package com.movingmaker.commentdiary.model.remote

import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.movingmaker.commentdiary.BuildConfig
import com.movingmaker.commentdiary.CodaApplication
import com.movingmaker.commentdiary.model.remote.api.*
import com.movingmaker.commentdiary.model.remote.response.ErrorResponse
import com.movingmaker.commentdiary.model.repository.ReIssueTokenRepository
import com.movingmaker.commentdiary.util.RetrofitHeaderCondition
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private val TAG: String? = "Interceptor"
    val onboardingApiService: OnboardingApiService by lazy {
        getRetrofit(RetrofitHeaderCondition.NO_HEADER).create(OnboardingApiService::class.java)
    }

    val myPageApiService: MyPageApiService by lazy {
        getRetrofit(RetrofitHeaderCondition.BEARER).create(MyPageApiService::class.java)
    }

    val myDiaryApiService: MyDiaryApiService by lazy{
        getRetrofit(RetrofitHeaderCondition.BEARER).create(MyDiaryApiService::class.java)
    }

    val reIssueTokenApiService: ReIssueTokenApiService by lazy{
        getRetrofit(RetrofitHeaderCondition.TWO_HEADER).create(ReIssueTokenApiService::class.java)
    }

    val logOutApiService: LogOutApiService by lazy{
        getRetrofit(RetrofitHeaderCondition.ONE_HEADER).create(LogOutApiService::class.java)
    }

    //Todo 일기작성시 accessToken만료됐는데 갱신 안 하는 경우 있는지 확인
    val gatherDiaryApiService: GatherDiaryApiService by lazy{
        getRetrofit(RetrofitHeaderCondition.BEARER).create(GatherDiaryApiService::class.java)
    }

    val receivedDiaryApiService: ReceivedDiaryApiService by lazy{
        getRetrofit(RetrofitHeaderCondition.BEARER).create(ReceivedDiaryApiService::class.java)
    }


//    private fun getSimpleRetrofit(): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl(Url.CODA_BASE_URL)
//            .addConverterFactory(
//                GsonConverterFactory.create(
//                    GsonBuilder()
//                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
//                        .create()
//                )
//            )
//            .client(buildOkHttpClient())
//            .build()
//    }

     private fun getRetrofit(headerCondition: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Url.BASE_URL)
            .client(buildHeaderOkHttpClient(headerCondition))
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
    private fun buildHeaderOkHttpClient(headerCondition: String): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        //todo Authenticator로 바꾸기
        when(headerCondition) {
            RetrofitHeaderCondition.NO_HEADER->{
                return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
            }
            RetrofitHeaderCondition.BEARER->{
                return OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
//            .authenticator(TokenAuthenticator())
                    .addInterceptor(BearerInterceptor())
                    .build()
            }
            RetrofitHeaderCondition.ONE_HEADER->{
                return OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(OneHeaderInterceptor())
                    .build()
            }
            RetrofitHeaderCondition.TWO_HEADER->{
                return OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(TwoHeaderInterceptor())
                    .build()
            }
        }
        return OkHttpClient.Builder().build()
    }


//    private fun buildOkHttpClient(): OkHttpClient {
//        val interceptor = HttpLoggingInterceptor()
//        if (BuildConfig.DEBUG) {
//            interceptor.level = HttpLoggingInterceptor.Level.BODY
//        } else {
//            interceptor.level = HttpLoggingInterceptor.Level.NONE
//        }
//
//        return OkHttpClient.Builder()
//            .connectTimeout(5, TimeUnit.SECONDS)
//            .addInterceptor(interceptor)
//            .build()
//    }

    //우선 로그아웃 api에 사용하는데 만료된 토큰 보내도 에러x
    class OneHeaderInterceptor: Interceptor{
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {

            val accessToken = runBlocking {
                CodaApplication.getInstance().getDataStore().accessToken.first()
            }
            val newRequest = chain.request().newBuilder().addHeader("X-AUTH-TOKEN", accessToken).build()
            return chain.proceed(newRequest)
        }
    }

    /*
    * bearer 토큰 필요한 api 사용시 accessToken유효한지 검사
    * 유효하지 않다면 재발급 api 호출
    * 재발급 api호출 시 refreshToken이 유효하지 않다면 error 401~404
    * 이 경우 로그아웃
    * refreshToken이 유효혀다면 정상적으로 accessToken재발급 후 기존 api 동작 완료
    * */
    class BearerInterceptor: Interceptor {
        //todo 조건 분기로 인터셉터 구조 변경
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {

            val accessTokenExpiresIn  = runBlocking {
                CodaApplication.getInstance().getDataStore().accessTokenExpiresIn.first()
            }
            var accessToken = ""

            if(accessTokenExpiresIn <= System.currentTimeMillis()){
                accessToken = runBlocking {
                    //토큰 갱신 api 호출
                    val response = ReIssueTokenRepository.INSTANCE.reIssueToken()
                    val errorResponse = response.errorBody()?.let { getErrorResponse(it) }
                    //refreshToken  만료된 경우
                    if(errorResponse?.status in 401 .. 404){
                        CodaApplication.getInstance().logOut()
                    }else {
                        try {
                            CodaApplication.getInstance().getDataStore().insertAuth(
                                response.body()!!.result.accessToken,
                                response.body()!!.result.refreshToken,
                                response.body()!!.result.accessTokenExpiresIn
                            )
                        } catch (e: Exception) {
                        }
                    }
                    response.body()?.result?.accessToken?: "Empty Token"
                }
            }
            else{
                accessToken = runBlocking {
                    CodaApplication.getInstance().getDataStore().accessToken.first()
                }
            }
            val newRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer ${accessToken}")
                .build()

            return chain.proceed(newRequest)
        }
    }


    class TwoHeaderInterceptor: Interceptor {
        //refreshtoken도 만료됐다면?? 로그아웃

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            //갱신 전 토큰들
            val refreshToken  = runBlocking {
                CodaApplication.getInstance().getDataStore().refreshToken.first()
            }
            val accessToken = runBlocking {
                CodaApplication.getInstance().getDataStore().accessToken.first()
            }
            //리프레쉬가 짧아서 갱신 테스트가 안

            val newRequest = chain.request().newBuilder().addHeader("X-AUTH-TOKEN", accessToken).addHeader("REFRESH-TOKEN", refreshToken)
                .build()

            return chain.proceed(newRequest)
        }
    }

    private fun getErrorResponse(errorBody: ResponseBody): ErrorResponse? {
//      errorBody로그로 찍고 그 담에 errorBody변수 사용하면 null값 들어옴.. 버근가?  Log.d("errorbody뭐들어오는데", errorBody.string())
        return getRetrofit(RetrofitHeaderCondition.TWO_HEADER).responseBodyConverter<ErrorResponse>(
            ErrorResponse::class.java,
            ErrorResponse::class.java.annotations
        ).convert(errorBody)
    }

    //    class TokenAuthenticator : Authenticator {
//        override fun authenticate(route: Route?, response: Response): Request? {
////            Log.d("111111111111", "Token " + "token")
//            if (response.request.header("Authorization") != null) {
////                Log.d("2222222222222", "Token " + "token")
//                return null
//            }
//
//            return response.request.newBuilder().header("Authorization", "Bearer ").build()
//        }
//    }
////    OkHttpClient.Builder().authenticator(TokenAuthenticator()).build()
}