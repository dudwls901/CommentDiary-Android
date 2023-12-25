package com.movingmaker.commentdiary.di


import com.movingmaker.data.local.datasource.DiaryLocalDataSource
import com.movingmaker.data.local.datasourceimpl.DiaryLocalDataSourceImpl
import com.movingmaker.data.remote.datasource.MemberRemoteDataSource
import com.movingmaker.data.remote.datasourceimpl.MemberRemoteDataSourceImpl
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
    abstract fun bindDiaryLocalDataSource(
        diaryLocalDataSourceImpl: DiaryLocalDataSourceImpl
    ): DiaryLocalDataSource
}