package com.movingmaker.commentdiary.di

import com.movingmaker.commentdiary.data.remote.datasource.ForSignUpDataSource
import com.movingmaker.commentdiary.data.remote.datasource.GatherDiaryDataSource
import com.movingmaker.commentdiary.data.remote.datasource.MyDiaryDataSource
import com.movingmaker.commentdiary.data.remote.datasource.MyPageDataSource
import com.movingmaker.commentdiary.data.remote.datasource.ReIssueTokenDataSource
import com.movingmaker.commentdiary.data.remote.datasource.ReceivedDiaryDataSource
import com.movingmaker.commentdiary.data.remote.datasourceimpl.ForSignUpDataSourceImpl
import com.movingmaker.commentdiary.data.remote.datasourceimpl.GatherDiaryDataSourceImpl
import com.movingmaker.commentdiary.data.remote.datasourceimpl.MyDiaryDataSourceImpl
import com.movingmaker.commentdiary.data.remote.datasourceimpl.MyPageDataSourceImpl
import com.movingmaker.commentdiary.data.remote.datasourceimpl.ReIssueTokenDataSourceImpl
import com.movingmaker.commentdiary.data.remote.datasourceimpl.ReceivedDiaryDataSourceImpl
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
    abstract fun bindForSignUpDataSource(
        forSignUpDataSourceImpl: ForSignUpDataSourceImpl,
    ): ForSignUpDataSource

    @Binds
    @Singleton
    abstract fun bindGatherDiaryDataSource(
        gatherDiaryDataSourceImpl: GatherDiaryDataSourceImpl,
    ): GatherDiaryDataSource

    @Binds
    @Singleton
    abstract fun bindMyDiaryDataSource(
        myDiaryDataSourceImpl: MyDiaryDataSourceImpl,
    ): MyDiaryDataSource

    @Binds
    @Singleton
    abstract fun bindMyPageDataSource(
        myPageDataSourceImpl: MyPageDataSourceImpl,
    ): MyPageDataSource

    @Binds
    @Singleton
    abstract fun bindReceivedDiaryDataSource(
        receivedDiaryDataSourceImpl: ReceivedDiaryDataSourceImpl,
    ): ReceivedDiaryDataSource

    @Binds
    @Singleton
    abstract fun bindReIssueTokenDataSource(
        reIssueTokenDataSourceImpl: ReIssueTokenDataSourceImpl,
    ): ReIssueTokenDataSource
}