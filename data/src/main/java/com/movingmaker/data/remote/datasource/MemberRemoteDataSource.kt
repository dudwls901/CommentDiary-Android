package com.movingmaker.data.remote.datasource

import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.User

interface MemberRemoteDataSource {
    suspend fun registerUser(user: User): NetworkResult<Unit>
}