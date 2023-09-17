package com.movingmaker.commentdiary.di.network

import com.movingmaker.commentdiary.BuildConfig
import com.movingmaker.data.remote.datasource.MemberRemoteDataSource
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.getErrorMessage
import com.movingmaker.presentation.util.PreferencesUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import javax.inject.Provider
import javax.inject.Qualifier
import javax.inject.Singleton

/*
* 1. Bearer
* 2. Bearer + X-Auth-Token //로그아웃
* 3. X-Auth-Token + REFRESH-TOKEN //토큰재발급
* 4. noheader
* */
@Module
@InstallIn(SingletonComponent::class)
object InterceptorModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class BearerInterceptor

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class XAuthTokenAndRefreshTokenHeaderInterceptor

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class BearerAndXAuthTokenHeaderInterceptor

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Singleton
    @XAuthTokenAndRefreshTokenHeaderInterceptor
    fun provideXAuthTokenAndRefreshTokenHeaderInterceptor(
        preferencesUtil: PreferencesUtil
    ) = Interceptor { chain ->
        //갱신 전 토큰들
        val refreshToken = preferencesUtil.getRefreshToken()
        val accessToken = preferencesUtil.getAccessToken()
        val newRequest = chain
            .request()
            .newBuilder()
            .addHeader("X-AUTH-TOKEN", accessToken)
            .addHeader("REFRESH-TOKEN", refreshToken)
            .build()
        chain.proceed(newRequest)
    }

    @Provides
    @Singleton
    @BearerAndXAuthTokenHeaderInterceptor
    fun provideBearerAndXAuthTokenHeaderInterceptor(
        provider: Provider<MemberRemoteDataSource>,
        preferencesUtil: PreferencesUtil
    ) = Interceptor { chain ->
        val accessToken = getBearerToken(provider.get(), preferencesUtil)
        val newRequest = chain
            .request()
            .newBuilder()
            .addHeader("Authorization", "Bearer ${accessToken}")
            .addHeader("X-AUTH-TOKEN", accessToken)
            .build()
        chain.proceed(newRequest)
    }

    @Provides
    @Singleton
    @BearerInterceptor
    fun provideBearerInterceptor(
        provider: Provider<MemberRemoteDataSource>,
        preferencesUtil: PreferencesUtil
    ) = Interceptor { chain ->
        val newRequest = chain
            .request()
            .newBuilder()
            .addHeader(
                "Authorization",
                "Bearer ${getBearerToken(provider.get(), preferencesUtil)}"
            )
            .build()
        Timber.d("intercept: $newRequest")
        chain.proceed(newRequest)
    }

    private fun getBearerToken(
        memberRemoteDataSource: MemberRemoteDataSource,
        preferencesUtil: PreferencesUtil
    ): String {
        val accessTokenExpiresIn = preferencesUtil.getAccessExpiresIn()
        return if (accessTokenExpiresIn <= System.currentTimeMillis()) {
            synchronized(this) {
                runBlocking {
                    //토큰 갱신 api 호출
                    val response = memberRemoteDataSource.reIssueToken()
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
                                        com.movingmaker.commentdiary.CodaApplication.getCustomExpire()
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
    }
}