package com.movingmaker.data.remote.datasourceimpl

import com.movingmaker.data.remote.api.FirebaseApiService
import com.movingmaker.data.remote.datasource.MemberRemoteDataSource
import com.movingmaker.data.util.safeApiCall
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.User
import javax.inject.Inject

class MemberRemoteDataSourceImpl @Inject constructor(
    private val firebaseApiService: FirebaseApiService,
) : MemberRemoteDataSource {
    override suspend fun registerUser(user: User): NetworkResult<Unit> =
        safeApiCall { firebaseApiService.registerUser(user) }
}