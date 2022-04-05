package com.movingmaker.commentdiary.model.remote.api

import com.movingmaker.commentdiary.model.remote.Url
import com.movingmaker.commentdiary.model.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.model.remote.response.CommentListResponse
import com.movingmaker.commentdiary.model.remote.response.CommentPushStateResponse
import com.movingmaker.commentdiary.model.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.model.remote.response.MyPageResponse
import retrofit2.Response
import retrofit2.http.*


interface MyPageApiService {
//    @Headers("Authorization: Bearer ")
    @DELETE(Url.MEMBERS)
    suspend fun signOut(): Response<IsSuccessResponse>

    @PATCH(Url.MEMBERS)
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): Response<IsSuccessResponse>

    @GET(Url.MEMBERS)
    suspend fun getMyPage(): Response<MyPageResponse>

    @GET(Url.COMMENT+Url.ALL)
    suspend fun getAllComment(): Response<CommentListResponse>
    //날짜 조회 코멘트
    @GET(Url.COMMENT)
    suspend fun getMonthComment(@Query("date") date: String): Response<CommentListResponse>

    @PATCH(Url.MEMBERS+Url.PUSH)
    suspend fun patchCommentPushState(): Response<CommentPushStateResponse>
}