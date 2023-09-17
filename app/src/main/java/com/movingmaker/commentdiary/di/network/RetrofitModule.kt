package com.movingmaker.commentdiary.di.network

import com.movingmaker.data.util.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
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
    annotation class XAuthTokenAndRefreshTokenRetrofit

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class BearerAndXAuthTokenRetrofit

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class BearerRetrofit

    @Singleton
    @Provides
    @NoHeaderRetrofit
    fun provideNoHeaderRetrofit(
        @OkHttpClientModule.NoHeaderInterceptorHttpClient okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @Singleton
    @XAuthTokenAndRefreshTokenRetrofit
    fun provideTwoHeaderRetrofit(
        @OkHttpClientModule.XAuthTokenAndRefreshTokenHeaderInterceptorHttpClient okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @Singleton
    @BearerAndXAuthTokenRetrofit
    fun provideOneHeaderRetrofit(
        @OkHttpClientModule.BearerAndXAuthTokenHeaderInterceptorHttpClient okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @Singleton
    @BearerRetrofit
    fun provideBearerRetrofit(
        @OkHttpClientModule.BearerInterceptorHttpClient okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }
}