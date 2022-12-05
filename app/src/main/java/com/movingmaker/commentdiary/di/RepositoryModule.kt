package com.movingmaker.commentdiary.di


import com.movingmaker.data.repositoryimpl.ForSignUpRepositoryImpl
import com.movingmaker.data.repositoryimpl.GatherDiaryRepositoryImpl
import com.movingmaker.data.repositoryimpl.MyDiaryRepositoryImpl
import com.movingmaker.data.repositoryimpl.MyPageRepositoryImpl
import com.movingmaker.data.repositoryimpl.ReceivedDiaryRepositoryImpl
import com.movingmaker.domain.repository.ForSignUpRepository
import com.movingmaker.domain.repository.GatherDiaryRepository
import com.movingmaker.domain.repository.MyDiaryRepository
import com.movingmaker.domain.repository.MyPageRepository
import com.movingmaker.domain.repository.ReceivedDiaryRepository
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