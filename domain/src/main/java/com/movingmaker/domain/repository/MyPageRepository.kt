package com.movingmaker.domain.repository

import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.MyInfo

interface MyPageRepository {
    suspend fun patchCommentPushState(): NetworkResult<BaseResponse<Map<String, Boolean>>>

    suspend fun logOut(): NetworkResult<BaseResponse<String>>

    suspend fun signOut(): NetworkResult<BaseResponse<String>>

    suspend fun getMyPage(): NetworkResult<BaseResponse<MyInfo>>
}