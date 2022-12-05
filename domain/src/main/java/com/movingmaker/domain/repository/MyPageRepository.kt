package com.movingmaker.domain.repository

import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.request.ChangePasswordModel
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.Comment
import com.movingmaker.domain.model.response.MyInfo

interface MyPageRepository {

    suspend fun signOut(): NetworkResult<BaseResponse<String>>

    suspend fun changePassword(changePasswordModel: ChangePasswordModel): NetworkResult<BaseResponse<String>>

    suspend fun getMyPage(): NetworkResult<BaseResponse<MyInfo>>

    suspend fun getAllComment(): NetworkResult<BaseResponse<List<Comment>>>

    suspend fun getMonthComment(date: String): NetworkResult<BaseResponse<List<Comment>>>

    suspend fun patchCommentPushState(): NetworkResult<BaseResponse<Map<String, Char>>>

    suspend fun logOut(): NetworkResult<BaseResponse<String>>
}