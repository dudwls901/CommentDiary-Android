package com.movingmaker.commentdiary.common.di

import com.movingmaker.commentdiary.data.repositoryimpl.*
import com.movingmaker.commentdiary.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindForSignUpRepository(
        forSignUpRepositoryImpl: ForSignUpRepositoryImpl,
    ): ForSignUpRepository

    @Binds
    @Singleton
    abstract fun bindGatherDiaryRepository(
        gatherDiaryRepositoryImpl: GatherDiaryRepositoryImpl,
    ): GatherDiaryRepository

    @Binds
    @Singleton
    abstract fun bindLogOutRepository(
        logOutRepositoryImpl: LogOutRepositoryImpl,
    ): LogOutRepository

    @Binds
    @Singleton
    abstract fun bindMyDiaryRepository(
        myDiaryRepositoryImpl: MyDiaryRepositoryImpl,
    ): MyDiaryRepository

    @Binds
    @Singleton
    abstract fun bindMyPageRepository(
        myPageRepositoryImpl: MyPageRepositoryImpl,
    ): MyPageRepository

    @Binds
    @Singleton
    abstract fun bindReceivedDiaryRepository(
        receivedDiaryRepositoryImpl: ReceivedDiaryRepositoryImpl,
    ): ReceivedDiaryRepository

}