package com.movingmaker.commentdiary.di


import com.movingmaker.data.local.datasource.DiaryLocalDataSource
import com.movingmaker.data.local.datasourceimpl.DiaryLocalDataSourceImpl
import com.movingmaker.data.remote.datasource.CommentRemoteDataSource
import com.movingmaker.data.remote.datasource.DiaryRemoteDataSource
import com.movingmaker.data.remote.datasource.MemberRemoteDataSource
import com.movingmaker.data.remote.datasource.MyPageRemoteDataSource
import com.movingmaker.data.remote.datasource.ReIssueTokenRemoteDataSource
import com.movingmaker.data.remote.datasource.ReceivedDiaryRemoteDataSource
import com.movingmaker.data.remote.datasource.ReportRemoteDataSource
import com.movingmaker.data.remote.datasourceimpl.CommentRemoteDataSourceImpl
import com.movingmaker.data.remote.datasourceimpl.DiaryRemoteDataSourceImpl
import com.movingmaker.data.remote.datasourceimpl.MemberRemoteDataSourceImpl
import com.movingmaker.data.remote.datasourceimpl.MyPageRemoteDataSourceImpl
import com.movingmaker.data.remote.datasourceimpl.ReIssueTokenRemoteDataSourceImpl
import com.movingmaker.data.remote.datasourceimpl.ReceivedDiaryRemoteDataSourceImpl
import com.movingmaker.data.remote.datasourceimpl.ReportRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindMemberRemoteDataSource(
        memberRemoteDataSourceImpl: MemberRemoteDataSourceImpl,
    ): MemberRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindDiaryRemoteDataSource(
        diaryRemoteDataSourceImpl: DiaryRemoteDataSourceImpl,
    ): DiaryRemoteDataSource


    @Binds
    @Singleton
    abstract fun bindReceivedDiaryRemoteDataSource(
        receivedDiaryDataRemoteSourceImpl: ReceivedDiaryRemoteDataSourceImpl,
    ): ReceivedDiaryRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindCommentRemoteDataSource(
        commentRemoteDataSourceImpl: CommentRemoteDataSourceImpl
    ): CommentRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindReIssueTokenRemoteDataSource(
        reIssueTokenRemoteDataSourceImpl: ReIssueTokenRemoteDataSourceImpl,
    ): ReIssueTokenRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindMyPageRemoteDataSource(
        myPageRemoteDataSourceImpl: MyPageRemoteDataSourceImpl,
    ): MyPageRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindReportRemoteDataSource(
        reportRemoteDataSourceImpl: ReportRemoteDataSourceImpl
    ): ReportRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindDiaryLocalDataSource(
        diaryLocalDataSourceImpl: DiaryLocalDataSourceImpl
    ): DiaryLocalDataSource
}