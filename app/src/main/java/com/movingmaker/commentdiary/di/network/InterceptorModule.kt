package com.movingmaker.commentdiary.di.network

import com.movingmaker.commentdiary.data.remote.datasource.ReIssueTokenDataSource
import com.movingmaker.commentdiary.domain.model.NetworkResult
import com.movingmaker.commentdiary.domain.model.getErrorMessage
import com.movingmaker.commentdiary.presentation.CodaApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import timber.log.Timber
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InterceptorModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class OneHeaderInterceptor

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class TwoHeaderInterceptor

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class BearerInterceptor

    @Provides
    @Singleton
    @OneHeaderInterceptor
    fun provideOneHeaderInterceptor() = Interceptor { chain ->
        val accessToken = CodaApplication.getInstance().getAccessToken()
        val newRequest =
            chain.request().newBuilder().addHeader("X-AUTH-TOKEN", accessToken).build()
        chain.proceed(newRequest)
    }

    @Provides
    @Singleton
    @TwoHeaderInterceptor
    fun provideTwoHeaderInterceptor() = Interceptor { chain ->
        //갱신 전 토큰들
        val refreshToken = CodaApplication.getInstance().getRefreshToken()
        val accessToken = CodaApplication.getInstance().getAccessToken()

        val newRequest = chain.request().newBuilder().addHeader("X-AUTH-TOKEN", accessToken)
            .addHeader("REFRESH-TOKEN", refreshToken)
            .build()
        chain.proceed(newRequest)
    }

    @Provides
    @Singleton
    @BearerInterceptor
    fun provideBearerInterceptor(
        reIssueTokenDataSource: ReIssueTokenDataSource,
    ) = Interceptor { chain ->
        val accessTokenExpiresIn = CodaApplication.getInstance().getAccessExpiresIn()
        val accessToken = if (accessTokenExpiresIn <= System.currentTimeMillis()) {
            synchronized(this) {
                runBlocking {
                    //토큰 갱신 api 호출
                    val response = reIssueTokenDataSource.reIssueToken()
                    var ret = "EMPTY"
                    with(response) {
                        when (this) {
                            is NetworkResult.Success -> {
                                (this.data.result).also { authTokens ->
                                    CodaApplication.getInstance().insertAuth(
                                        CodaApplication.getInstance().getLoginType(),
                                        authTokens.accessToken,
                                        authTokens.refreshToken,
                                        //todo 현재 서버에서 받은 만료 시간이 아닌 커스텀 만료 시간 (현재 시간 + 1초) -> release 버전엔 서버 데이터
//                                        authTokens.accessTokenExpiresIn
                                        CodaApplication.getCustomExpire()
                                    )
                                    ret = authTokens.accessToken
                                }
                            }
                            is NetworkResult.Fail -> {
                                CodaApplication.getInstance().logOut()
                                Timber.e("Reissue Fail : ${this.message}")
                            }
                            is NetworkResult.Exception -> {
                                Timber.e("ReIssue Error : ${this.errorType.getErrorMessage()}")
                            }
                        }
                    }
                    ret
                }
            }
        } else {
            CodaApplication.getInstance().getAccessToken()
        }
        val newRequest =
            chain.request().newBuilder().addHeader("Authorization", "Bearer $accessToken")
                .build()
        Timber.d("intercept: $newRequest")
        chain.proceed(newRequest)
    }
}