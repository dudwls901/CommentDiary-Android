package com.movingmaker.commentdiary.data.remote.datasource

import com.movingmaker.commentdiary.data.model.Comment
import com.movingmaker.commentdiary.data.model.MyInfo
import com.movingmaker.commentdiary.data.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.domain.model.BaseResponse
import com.movingmaker.commentdiary.domain.model.NetworkResult

interface MyPageDataSource {

    suspend fun signOut(): NetworkResult<BaseResponse<String>>

    suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): NetworkResult<BaseResponse<String>>

    suspend fun getMyPage(): NetworkResult<BaseResponse<MyInfo>>

    suspend fun getAllComment(): NetworkResult<BaseResponse<List<Comment>>>

    suspend fun getMonthComment(date: String): NetworkResult<BaseResponse<List<Comment>>>

    suspend fun patchCommentPushState(): NetworkResult<BaseResponse<Map<String,Char>>>

    suspend fun logOut(): NetworkResult<BaseResponse<String>>

}