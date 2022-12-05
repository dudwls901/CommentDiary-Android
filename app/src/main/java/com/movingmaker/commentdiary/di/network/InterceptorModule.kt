package com.movingmaker.commentdiary.di.network

import com.movingmaker.commentdiary.CodaApplication
import com.movingmaker.data.remote.datasource.ReIssueTokenDataSource
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.getErrorMessage
import com.movingmaker.presentation.util.PreferencesUtil
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
    fun provideOneHeaderInterceptor(
        preferencesUtil: PreferencesUtil
    ) = Interceptor { chain ->
        val accessToken = preferencesUtil.getAccessToken()
        val newRequest =
            chain.request().newBuilder().addHeader("X-AUTH-TOKEN", accessToken).build()
        chain.proceed(newRequest)
    }

    @Provides
    @Singleton
    @TwoHeaderInterceptor
    fun provideTwoHeaderInterceptor(
        preferencesUtil: PreferencesUtil
    ) = Interceptor { chain ->
        //갱신 전 토큰들
        val refreshToken = preferencesUtil.getRefreshToken()
        val accessToken = preferencesUtil.getAccessToken()
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
        preferencesUtil: PreferencesUtil
    ) = Interceptor { chain ->
        val accessTokenExpiresIn = preferencesUtil.getAccessExpiresIn()
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
                                    preferencesUtil.insertAuth(
                                        preferencesUtil.getLoginType(),
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
                                preferencesUtil.logOut()
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
            preferencesUtil.getAccessToken()
        }
        val newRequest =
            chain.request().newBuilder().addHeader("Authorization", "Bearer $accessToken")
                .build()
        Timber.d("intercept: $newRequest")
        chain.proceed(newRequest)
    }
}