package com.movingmaker.commentdiary.common.di.network

import com.movingmaker.commentdiary.BuildConfig
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
    annotation class OneHeaderInterceptorHttpClient

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class TwoHeaderInterceptorHttpClient


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
    @NoHeaderInterceptorHttpClient
    //okHttp 의존성 주입 (아래 retrofit 의존성 주입에 사용)
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
    //okHttp 의존성 주입 (아래 retrofit 의존성 주입에 사용)
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
    @OneHeaderInterceptorHttpClient
    //okHttp 의존성 주입 (아래 retrofit 의존성 주입에 사용)
    fun provideOneHeaderHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        @InterceptorModule.OneHeaderInterceptor oneHeaderInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(oneHeaderInterceptor)
            .build()

    }

    @Provides
    @Singleton
    @TwoHeaderInterceptorHttpClient
    //okHttp 의존성 주입 (아래 retrofit 의존성 주입에 사용)
    fun provideTwoHeaHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        @InterceptorModule.TwoHeaderInterceptor twoHeaderInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(twoHeaderInterceptor)
            .build()
    }
}