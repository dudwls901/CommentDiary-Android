package com.movingmaker.commentdiary.data.remote.datasource

import com.movingmaker.commentdiary.data.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.data.remote.response.CommentListResponse
import com.movingmaker.commentdiary.data.remote.response.CommentPushStateResponse
import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.data.remote.response.MyPageResponse
import retrofit2.Response

interface MyPageDataSource {

    suspend fun signOut(): Response<IsSuccessResponse>

    suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): Response<IsSuccessResponse>

    suspend fun getMyPage(): Response<MyPageResponse>

    suspend fun getAllComment(): Response<CommentListResponse>

    suspend fun getMonthComment(date: String): Response<CommentListResponse>

    suspend fun patchCommentPushState(): Response<CommentPushStateResponse>

}