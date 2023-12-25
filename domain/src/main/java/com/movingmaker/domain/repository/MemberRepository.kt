package com.movingmaker.domain.repository

import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.User

interface MemberRepository {
    suspend fun registerUser(user: User): NetworkResult<Unit>
}
