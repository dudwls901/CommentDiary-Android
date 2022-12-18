package com.movingmaker.data.remote.datasource

import com.movingmaker.data.remote.model.request.ChangePasswordRequest
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.Comment
import com.movingmaker.domain.model.response.MyInfo

interface MyPageDataSource {

    suspend fun signOut(): NetworkResult<BaseResponse<String>>

    suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): NetworkResult<BaseResponse<String>>

    suspend fun getMyPage(): NetworkResult<BaseResponse<MyInfo>>

    suspend fun getAllComment(): NetworkResult<BaseResponse<List<Comment>>>

    suspend fun getPeriodComment(date: String): NetworkResult<BaseResponse<List<Comment>>>

    suspend fun patchCommentPushState(): NetworkResult<BaseResponse<Map<String, Char>>>

    suspend fun logOut(): NetworkResult<BaseResponse<String>>

}