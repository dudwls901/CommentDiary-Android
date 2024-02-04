package com.movingmaker.commentdiary.di.network

import com.movingmaker.commentdiary.BASE_URL_DEV
import com.movingmaker.commentdiary.BASE_URL_PROD
import com.movingmaker.commentdiary.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    private val baseUrl = if (BuildConfig.DEBUG) BASE_URL_DEV else BASE_URL_PROD

    @Singleton
    @Provides
    fun provideFirebaseRetrofit(
        @OkHttpClientModule.FirebaseInterceptorHttpClient okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }
}