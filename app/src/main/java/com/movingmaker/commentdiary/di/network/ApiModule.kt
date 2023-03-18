package com.movingmaker.commentdiary.di.network


import com.movingmaker.data.remote.api.CommentApiService
import com.movingmaker.data.remote.api.DiaryApiService
import com.movingmaker.data.remote.api.MemberApiService
import com.movingmaker.data.remote.api.MyPageApiService
import com.movingmaker.data.remote.api.ReIssueTokenApiService
import com.movingmaker.data.remote.api.ReceivedDiaryApiService
import com.movingmaker.data.remote.api.ReportApiService
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
    fun provideMemberApiService(
        @RetrofitModule.NoHeaderRetrofit retrofit: Retrofit
    ): MemberApiService {
        return retrofit.create(MemberApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMyPageApiService(
        @RetrofitModule.BearerRetrofit retrofit: Retrofit
    ): MyPageApiService {
        return retrofit
            .create(MyPageApiService::class.java)
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
    fun provideDiaryApiService(
        @RetrofitModule.BearerRetrofit retrofit: Retrofit
    ): DiaryApiService {
        return retrofit
            .create(DiaryApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideReceivedDiaryApiService(
        @RetrofitModule.BearerRetrofit retrofit: Retrofit
    ): ReceivedDiaryApiService {
        return retrofit.create(ReceivedDiaryApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCommentApiService(
        @RetrofitModule.BearerRetrofit retrofit: Retrofit
    ): CommentApiService{
        return retrofit.create(CommentApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideReportApiService(
        @RetrofitModule.BearerRetrofit retrofit: Retrofit
    ): ReportApiService{
        return retrofit.create(ReportApiService::class.java)
    }
}
