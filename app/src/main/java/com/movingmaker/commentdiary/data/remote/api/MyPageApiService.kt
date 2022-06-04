package com.movingmaker.commentdiary.data.remote.api

import com.movingmaker.commentdiary.data.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.data.remote.response.CommentListResponse
import com.movingmaker.commentdiary.data.remote.response.CommentPushStateResponse
import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.data.remote.response.MyPageResponse
import com.movingmaker.commentdiary.util.Url.ALL
import com.movingmaker.commentdiary.util.Url.COMMENT
import com.movingmaker.commentdiary.util.Url.MEMBERS
import com.movingmaker.commentdiary.util.Url.PUSH
import retrofit2.Response
import retrofit2.http.*


interface MyPageApiService {
//    @Headers("Authorization: Bearer ")
    @DELETE(MEMBERS)
    suspend fun signOut(): Response<IsSuccessResponse>

    @PATCH(MEMBERS)
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): Response<IsSuccessResponse>

    @GET(MEMBERS)
    suspend fun getMyPage(): Response<MyPageResponse>

    @GET(COMMENT+ALL)
    suspend fun getAllComment(): Response<CommentListResponse>
    //날짜 조회 코멘트
    @GET(COMMENT)
    suspend fun getMonthComment(@Query("date") date: String): Response<CommentListResponse>

    @PATCH(MEMBERS+PUSH)
    suspend fun patchCommentPushState(): Response<CommentPushStateResponse>
}