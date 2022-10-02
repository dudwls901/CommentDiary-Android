package com.movingmaker.commentdiary.common.di.network

import com.movingmaker.commentdiary.common.util.Url
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class NoHeaderRetrofit

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class OneHeaderRetrofit

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class TwoHeaderRetrofit

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class BearerRetrofit

    @Singleton
    @Provides
    @NoHeaderRetrofit
    fun provideNoHeaderRetrofit(
        @OkHttpClientModule.NoHeaderInterceptorHttpClient okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        scalarsConverterFactory: ScalarsConverterFactory,
        nullOnEmptyConverterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Url.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(scalarsConverterFactory)
            .addConverterFactory(nullOnEmptyConverterFactory)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    @TwoHeaderRetrofit
    fun provideTwoHeaderRetrofit(
        @OkHttpClientModule.TwoHeaderInterceptorHttpClient okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        scalarsConverterFactory: ScalarsConverterFactory,
        nullOnEmptyConverterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Url.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(scalarsConverterFactory)
            .addConverterFactory(nullOnEmptyConverterFactory)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    @OneHeaderRetrofit
    fun provideOneHeaderRetrofit(
        @OkHttpClientModule.OneHeaderInterceptorHttpClient okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        scalarsConverterFactory: ScalarsConverterFactory,
        nullOnEmptyConverterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Url.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(scalarsConverterFactory)
            .addConverterFactory(nullOnEmptyConverterFactory)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    @BearerRetrofit
    fun provideBearerRetrofit(
        @OkHttpClientModule.BearerInterceptorHttpClient okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        scalarsConverterFactory: ScalarsConverterFactory,
        nullOnEmptyConverterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Url.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(scalarsConverterFactory)
            .addConverterFactory(nullOnEmptyConverterFactory)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }
}