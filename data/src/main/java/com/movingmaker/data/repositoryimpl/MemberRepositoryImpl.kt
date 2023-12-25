package com.movingmaker.data.repositoryimpl

import com.movingmaker.data.remote.datasource.MemberRemoteDataSource
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.User
import com.movingmaker.domain.repository.MemberRepository
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(
    private val memberRemoteDataSource: MemberRemoteDataSource,
) : MemberRepository {
    override suspend fun registerUser(user: User): NetworkResult<Unit> =
        memberRemoteDataSource.registerUser(user)
}