package com.movingmaker.commentdiary.common.di

import com.movingmaker.commentdiary.data.remote.datasource.*
import com.movingmaker.commentdiary.data.remote.datasourceimpl.*
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
    abstract fun bindLogOutDataSource(
        logOutDataSourceImpl: LogOutDataSourceImpl,
    ): LogOutDataSource

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