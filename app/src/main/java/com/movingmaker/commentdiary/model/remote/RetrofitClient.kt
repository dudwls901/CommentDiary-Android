package com.movingmaker.commentdiary.model.remote

import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.movingmaker.commentdiary.BuildConfig
import com.movingmaker.commentdiary.CodaApplication
import com.movingmaker.commentdiary.model.remote.api.MyDiaryApiService
import com.movingmaker.commentdiary.model.remote.api.MyPageApiService
import com.movingmaker.commentdiary.model.remote.api.OnboardingApiService
import com.movingmaker.commentdiary.model.remote.api.ReIssueTokenApiService
import com.movingmaker.commentdiary.model.remote.response.ErrorResponse
import com.movingmaker.commentdiary.model.repository.ReIssueTokenRepository
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
        getSimpleRetrofit().create(OnboardingApiService::class.java)
    }

    val myPageApiService: MyPageApiService by lazy {
        getAuthRetrofit(1).create(MyPageApiService::class.java)
    }

    val myDiaryApiService: MyDiaryApiService by lazy{
        getAuthRetrofit(1).create(MyDiaryApiService::class.java)
    }

    val reIssueTokenApiService: ReIssueTokenApiService by lazy{
        getAuthRetrofit(2).create(ReIssueTokenApiService::class.java)
    }

    private fun getSimpleRetrofit(): Retrofit {
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

     fun getAuthRetrofit(headerCount: Int): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Url.BASE_URL)
            .client(buildHeaderOkHttpClient(headerCount))
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
    private fun buildHeaderOkHttpClient(headerCount: Int): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        //todo Authenticator로 바꾸기
        if(headerCount==1) {
            return OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
//            .authenticator(TokenAuthenticator())
                .addInterceptor(HeaderInterceptor())
                .build()
        }
        else{
            return OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(ReIssueHeaderInterceptor())
                .build()
        }
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
            .build()
    }

    /*
    * bearer 토큰 필요한 api 사용시 accessToken유효한지 검사
    * 유효하지 않다면 재발급 api 호출
    * 재발급 api호출 시 refreshToken이 유효하지 않다면 error 401~404
    * 이 경우 로그아웃
    * refreshToken이 유효혀다면 정상적으로 accessToken재발급 후 기존 api 동작 완료
    * */
    class HeaderInterceptor: Interceptor {
        //todo 조건 분기로 인터셉터 구조 변경
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {

            val accessTokenExpiresIn  = runBlocking {
                CodaApplication.getInstance().getDataStore().accessTokenExpiresIn.first()
            }
            var accessToken = ""
//            val date = Date(accessTokenExpiresIn)
            val simpleDateFormatTime = SimpleDateFormat("MM-DD HH:mm:ss")

            Log.d("만료 시간!!!!!!!!!!!!!!!!!!", simpleDateFormatTime.format(Date(accessTokenExpiresIn)) + " 지금 시간 : " + simpleDateFormatTime.format(Date(accessTokenExpiresIn)))

            if(accessTokenExpiresIn <= System.currentTimeMillis()){

                Log.d(TAG, "intercept: accessToken 만료됨 ")
                accessToken = runBlocking {
                    //토큰 갱신 api 호출
                    val response = ReIssueTokenRepository.INSTANCE.reIssueToken()
                    val errorResponse = response.errorBody()?.let { getErrorResponse(it) }
                    Log.d("ERRORBODYCONVERT", errorResponse?.code?:"no Error")
                    Log.d("ERRORBODYCONVERT", errorResponse?.message?:"no Error")
                    Log.d("ERRORBODYCONVERT", errorResponse?.status.toString())
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
                            Log.d(TAG, e.toString())
                            Log.d(TAG, "갱신한 토큰을 데이터스토어에 저장하는 데 실패하였습니다. ")
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
            Log.d("뭔데",  accessToken)
            val newRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer ${accessToken}")
                .build()
            Log.d("REQUEST@@@@@@@@@@@@@@@@@@@@@@@@",  newRequest.toString())
            return chain.proceed(newRequest)
        }
    }


    class ReIssueHeaderInterceptor: Interceptor {
        //todo refreshtoken도 만료됐다면??

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
            Log.d("reIssue REQUEST@@@@@@@@@@@@@@@@@@@@@@@@",  newRequest.toString())
            return chain.proceed(newRequest)
        }
    }

    private fun getErrorResponse(errorBody: ResponseBody): ErrorResponse? {
//      errorBody로그로 찍고 그 담에 errorBody변수 사용하면 null값 들어옴.. 버근가?  Log.d("errorbody뭐들어오는데", errorBody.string())
        return getAuthRetrofit(2).responseBodyConverter<ErrorResponse>(
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