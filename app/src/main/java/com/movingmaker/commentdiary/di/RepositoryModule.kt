package com.movingmaker.commentdiary.di


import com.movingmaker.data.repositoryimpl.CommentRepositoryImpl
import com.movingmaker.data.repositoryimpl.DiaryRepositoryImpl
import com.movingmaker.data.repositoryimpl.MemberRepositoryImpl
import com.movingmaker.data.repositoryimpl.PreferencesRepositoryImpl
import com.movingmaker.data.repositoryimpl.ReceivedDiaryRepositoryImpl
import com.movingmaker.data.repositoryimpl.ReportRepositoryImpl
import com.movingmaker.domain.repository.CommentRepository
import com.movingmaker.domain.repository.DiaryRepository
import com.movingmaker.domain.repository.MemberRepository
import com.movingmaker.domain.repository.PreferencesRepository
import com.movingmaker.domain.repository.ReceivedDiaryRepository
import com.movingmaker.domain.repository.ReportRepository
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
    abstract fun bindMemberRepository(
        memberRepositoryImpl: MemberRepositoryImpl,
    ): MemberRepository

    @Binds
    @Singleton
    abstract fun bindCommentRepository(
        commentRepositoryImpl: CommentRepositoryImpl,
    ): CommentRepository

    @Binds
    @Singleton
    abstract fun bindReportRepository(
        reportRepositoryImpl: ReportRepositoryImpl,
    ): ReportRepository

    @Binds
    @Singleton
    abstract fun bindDiaryRepository(
        diaryRepositoryImpl: DiaryRepositoryImpl,
    ): DiaryRepository


    @Binds
    @Singleton
    abstract fun bindReceivedDiaryRepository(
        receivedDiaryRepositoryImpl: ReceivedDiaryRepositoryImpl,
    ): ReceivedDiaryRepository

    @Binds
    @Singleton
    abstract fun bindPreferencesRepository(
        preferencesRepositoryImpl: PreferencesRepositoryImpl,
    ): PreferencesRepository

}