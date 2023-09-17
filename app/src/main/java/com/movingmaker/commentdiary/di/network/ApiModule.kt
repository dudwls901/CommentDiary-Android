package com.movingmaker.commentdiary.di.network


import com.movingmaker.data.remote.api.BearerAndXAuthTokenApiService
import com.movingmaker.data.remote.api.BearerApiService
import com.movingmaker.data.remote.api.NoHeaderApiService
import com.movingmaker.data.remote.api.XAuthTokenAndRefreshTokenApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideNoHeaderApiService(
        @RetrofitModule.NoHeaderRetrofit retrofit: Retrofit
    ): NoHeaderApiService {
        return retrofit.create(NoHeaderApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideXAuthTokenAndRefreshTokenApiService(
        @RetrofitModule.XAuthTokenAndRefreshTokenRetrofit retrofit: Retrofit
    ): XAuthTokenAndRefreshTokenApiService {
        return retrofit.create(XAuthTokenAndRefreshTokenApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideBearerAndXAuthTokenApiService(
        @RetrofitModule.BearerAndXAuthTokenRetrofit retrofit: Retrofit
    ): BearerAndXAuthTokenApiService {
        return retrofit.create(BearerAndXAuthTokenApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideBearerApiService(
        @RetrofitModule.BearerRetrofit retrofit: Retrofit
    ): BearerApiService {
        return retrofit.create(BearerApiService::class.java)
    }

}
