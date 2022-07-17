package com.movingmaker.commentdiary.data.remote

import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.movingmaker.commentdiary.BuildConfig
import com.movingmaker.commentdiary.global.CodaApplication
import com.movingmaker.commentdiary.data.remote.api.*
import com.movingmaker.commentdiary.data.remote.response.ErrorResponse
import com.movingmaker.commentdiary.data.repository.ReIssueTokenRepository
import com.movingmaker.commentdiary.util.RetrofitHeaderCondition
import com.movingmaker.commentdiary.util.Url.BASE_URL
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit


object RetrofitClient {
    private const val TAG = "Interceptor"
    val onboardingApiService: OnboardingApiService by lazy {
        getRetrofit(RetrofitHeaderCondition.NO_HEADER).create(OnboardingApiService::class.java)
    }

    val myPageApiService: MyPageApiService by lazy {
        getRetrofit(RetrofitHeaderCondition.BEARER).create(MyPageApiService::class.java)
    }

    val myDiaryApiService: MyDiaryApiService by lazy {
        getRetrofit(RetrofitHeaderCondition.BEARER).create(MyDiaryApiService::class.java)
    }

    val reIssueTokenApiService: ReIssueTokenApiService by lazy {
        getRetrofit(RetrofitHeaderCondition.TWO_HEADER).create(ReIssueTokenApiService::class.java)
    }

    val logOutApiService: LogOutApiService by lazy {
        getRetrofit(RetrofitHeaderCondition.ONE_HEADER).create(LogOutApiService::class.java)
    }

    val gatherDiaryApiService: GatherDiaryApiService by lazy {
        getRetrofit(RetrofitHeaderCondition.BEARER).create(GatherDiaryApiService::class.java)
    }

    val receivedDiaryApiService: ReceivedDiaryApiService by lazy {
        getRetrofit(RetrofitHeaderCondition.BEARER).create(ReceivedDiaryApiService::class.java)
    }

    @JvmStatic
    private fun getRetrofit(headerCondition: String): Retrofit {
//         var gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(buildHeaderOkHttpClient(headerCondition))
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .setLenient()
                        .create()
                )
            )
            .build()
    }

    //로깅 + 헤더
    @JvmStatic
    private fun buildHeaderOkHttpClient(headerCondition: String): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        //todo Authenticator로 바꾸기
        when (headerCondition) {
            RetrofitHeaderCondition.NO_HEADER -> {
                return OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .build()
            }
            RetrofitHeaderCondition.BEARER -> {
                return OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
//            .authenticator(TokenAuthenticator())
                    .addInterceptor(BearerInterceptor())
                    .build()
            }
            RetrofitHeaderCondition.ONE_HEADER -> {
                return OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(OneHeaderInterceptor())
                    .build()
            }
            RetrofitHeaderCondition.TWO_HEADER -> {
                return OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(TwoHeaderInterceptor())
                    .build()
            }
        }
        return OkHttpClient.Builder().build()
    }

    //우선 로그아웃 api에 사용하는데 만료된 토큰 보내도 에러x
    class OneHeaderInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {

//            val accessToken = runBlocking {
//                CodaApplication.getInstance().getDataStore().accessToken.first()
//            }
            val accessToken = CodaApplication.getInstance().getAccessToken()
            val newRequest =
                chain.request().newBuilder().addHeader("X-AUTH-TOKEN", accessToken).build()
            Log.d(TAG, "intercept: $newRequest")
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
    class BearerInterceptor : Interceptor {
        //todo 조건 분기로 인터셉터 구조 변경
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {

            val accessTokenExpiresIn = CodaApplication.getInstance().getAccessExpiresIn()
//            val accessTokenExpiresIn = getSyncAccessTokenExpiresIn()
            var accessToken =""
            Log.d(TAG, "intercept: --> ${accessTokenExpiresIn}")
            if (accessTokenExpiresIn <= System.currentTimeMillis()) {
                accessToken = runBlocking {
                    //토큰 갱신 api 호출
                    val response = ReIssueTokenRepository.INSTANCE.reIssueToken()
                    val errorResponse = response.errorBody()?.let { getErrorResponse(it) }
                    //refreshToken  만료된 경우
                    if (errorResponse?.status in 401..404) {
                        CodaApplication.getInstance().logOut()
                    } else {
                        try {
                            CodaApplication.getInstance().insertAuth(
                                CodaApplication.getInstance().getLoginType(),
                                response.body()!!.result.accessToken,
                                response.body()!!.result.refreshToken,
                                CodaApplication.getCustomExpire()
                            )
                        } catch (e: Exception) {
                            Log.d(TAG, "okhttp intercept: error $e")
                        }
                    }
                    response.body()?.result?.accessToken ?: "Empty Token"
                }
            } else {
                accessToken = CodaApplication.getInstance().getAccessToken()
//                accessToken = runBlocking {
//                    CodaApplication.getInstance().getDataStore().accessToken.first()
//                }
            }
            val newRequest =
                chain.request().newBuilder().addHeader("Authorization", "Bearer ${accessToken}")
                    .build()
            Log.d(TAG, "intercept: $newRequest")
            return chain.proceed(newRequest)
        }
    }


    class TwoHeaderInterceptor : Interceptor {
        //refreshtoken도 만료됐다면?? 로그아웃

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            //갱신 전 토큰들
            val refreshToken = CodaApplication.getInstance().getRefreshToken()
            val accessToken = CodaApplication.getInstance().getAccessToken()

            val newRequest = chain.request().newBuilder().addHeader("X-AUTH-TOKEN", accessToken)
                .addHeader("REFRESH-TOKEN", refreshToken)
                .build()
            Log.d(TAG, "okhttp twoHeader intercept: $newRequest")
            return chain.proceed(newRequest)
        }
    }

    fun getErrorResponse(errorBody: ResponseBody): ErrorResponse? {
//      errorBody로그로 찍고 그 담에 errorBody변수 사용하면 null값 들어옴.. 버근가?  Log.d("errorbody뭐들어오는데", errorBody.string())
        try {
            return getRetrofit(RetrofitHeaderCondition.TWO_HEADER).responseBodyConverter<ErrorResponse>(
                ErrorResponse::class.java,
                ErrorResponse::class.java.annotations
            ).convert(errorBody)
        }catch (e: Exception){
            Log.e(TAG, "getErrorResponse: ${e.message}")
        }
        finally {
            return null
        }
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

class NullOnEmptyConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type?,
        annotations: Array<Annotation>?,
        retrofit: Retrofit?
    ): Converter<ResponseBody, *> {
        val delegate = retrofit!!.nextResponseBodyConverter<Any>(this, type!!, annotations!!)
        return Converter<ResponseBody, Any> {
            if (it.contentLength() == 0L) return@Converter String
            delegate.convert(it)
        }
    }

}