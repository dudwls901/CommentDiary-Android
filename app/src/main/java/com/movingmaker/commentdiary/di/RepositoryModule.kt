package com.movingmaker.commentdiary.di


import com.movingmaker.data.repositoryimpl.MemberRepositoryImpl
import com.movingmaker.data.repositoryimpl.PreferencesRepositoryImpl
import com.movingmaker.domain.repository.MemberRepository
import com.movingmaker.domain.repository.PreferencesRepository
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
    abstract fun bindPreferencesRepository(
        preferencesRepositoryImpl: PreferencesRepositoryImpl,
    ): PreferencesRepository

}