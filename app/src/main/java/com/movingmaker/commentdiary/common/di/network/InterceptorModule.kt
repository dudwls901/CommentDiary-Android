package com.movingmaker.commentdiary.common.di.network

import com.movingmaker.commentdiary.common.CodaApplication
import com.movingmaker.commentdiary.data.remote.datasource.ReIssueTokenDataSource
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
        Timber.d("okhttp twoHeader intercept: $newRequest")
        chain.proceed(newRequest)
    }

    @Provides
    @Singleton
    @BearerInterceptor
    fun provideBearerInterceptor(
        reIssueTokenDataSource: ReIssueTokenDataSource
    ) = Interceptor { chain ->
        val accessTokenExpiresIn = CodaApplication.getInstance().getAccessExpiresIn()
//        val accessTokenExpiresIn = getSyncAccessTokenExpiresIn()
        var accessToken = CodaApplication.getInstance().getAccessToken()
        if (accessTokenExpiresIn <= System.currentTimeMillis()) {
            accessToken = runBlocking {
                //토큰 갱신 api 호출
//                val response = reIssueTokenRepository.reIssueToken()
                val response = reIssueTokenDataSource.reIssueToken()
                //refreshToken  만료된 경우
                if (response.code() in 401..404) {
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
                        Timber.e("okhttp intercept: error $e")
                    }
                }
                response.body()?.result?.accessToken ?: "Empty Token"
            }
        } else {
            accessToken = CodaApplication.getInstance().getAccessToken()
        }
        val newRequest =
            chain.request().newBuilder().addHeader("Authorization", "Bearer ${accessToken}")
                .build()
        Timber.d("intercept: $newRequest")
        chain.proceed(newRequest)
    }
}