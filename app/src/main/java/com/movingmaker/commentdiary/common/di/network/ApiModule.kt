package com.movingmaker.commentdiary.common.di.network

import com.movingmaker.commentdiary.data.remote.api.*
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
    fun provideOnboardingApiService(
        @RetrofitModule.NoHeaderRetrofit retrofit: Retrofit
    ): OnboardingApiService {
        return retrofit.create(OnboardingApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMyPageApiService(
        @RetrofitModule.BearerRetrofit retrofit: Retrofit
    ): MyPageApiService {
        return retrofit.create(MyPageApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMyDiaryApiService(
        @RetrofitModule.BearerRetrofit retrofit: Retrofit
    ): MyDiaryApiService {
        return retrofit
            .create(MyDiaryApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideReIssueTokenApiService(
        @RetrofitModule.TwoHeaderRetrofit retrofit: Retrofit
    ): ReIssueTokenApiService {
        return retrofit.create(ReIssueTokenApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideLogOutApiService(
        @RetrofitModule.OneHeaderRetrofit retrofit: Retrofit
    ): LogOutApiService {
        return retrofit.create(LogOutApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGatherDiaryApiService(
        @RetrofitModule.BearerRetrofit retrofit: Retrofit
    ): GatherDiaryApiService {
        return retrofit.create(GatherDiaryApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideReceivedDiaryApiService(
        @RetrofitModule.BearerRetrofit retrofit: Retrofit
    ): ReceivedDiaryApiService {
        return retrofit.create(ReceivedDiaryApiService::class.java)
    }
}