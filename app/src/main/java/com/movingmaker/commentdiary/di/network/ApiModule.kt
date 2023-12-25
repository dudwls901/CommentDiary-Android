package com.movingmaker.commentdiary.di.network


import com.movingmaker.data.remote.api.FirebaseApiService
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
    fun provideFirebaseApiService(
        retrofit: Retrofit,
    ): FirebaseApiService {
        return retrofit.create(FirebaseApiService::class.java)
    }
}
