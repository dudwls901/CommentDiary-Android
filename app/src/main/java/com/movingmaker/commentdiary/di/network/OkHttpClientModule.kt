package com.movingmaker.commentdiary.di.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OkHttpClientModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class NoHeaderInterceptorHttpClient

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class BearerInterceptorHttpClient

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class XAuthTokenAndRefreshTokenHeaderInterceptorHttpClient

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class BearerAndXAuthTokenHeaderInterceptorHttpClient

    @Provides
    @Singleton
    @NoHeaderInterceptorHttpClient
    fun provideNoHeaderInterceptorHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @BearerInterceptorHttpClient
    fun provideBearerInterceptorHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        @InterceptorModule.BearerInterceptor bearerInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(bearerInterceptor)
            .build()

    }

    @Provides
    @Singleton
    @XAuthTokenAndRefreshTokenHeaderInterceptorHttpClient
    fun provideOneHeaderHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        @InterceptorModule.XAuthTokenAndRefreshTokenHeaderInterceptor xAuthTokenAndRefreshTokenHeaderInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(xAuthTokenAndRefreshTokenHeaderInterceptor)
            .build()

    }

    @Provides
    @Singleton
    @BearerAndXAuthTokenHeaderInterceptorHttpClient
    fun provideTwoHeaHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        @InterceptorModule.BearerAndXAuthTokenHeaderInterceptor bearerAndXAuthTokenHeaderInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(bearerAndXAuthTokenHeaderInterceptor)
            .build()
    }
}