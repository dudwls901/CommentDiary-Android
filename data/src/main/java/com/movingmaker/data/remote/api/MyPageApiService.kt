package com.movingmaker.data.remote.api

import com.movingmaker.data.remote.model.request.ChangePasswordRequest
import com.movingmaker.data.remote.model.response.BaseResponse
import com.movingmaker.data.remote.model.response.CommentResponse
import com.movingmaker.data.remote.model.response.MyInfoResponse
import com.movingmaker.data.util.ALL
import com.movingmaker.data.util.COMMENT
import com.movingmaker.data.util.MEMBERS
import com.movingmaker.data.util.PUSH
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query


interface MyPageApiService {
    @DELETE(MEMBERS)
    suspend fun signOut(): Response<BaseResponse<String>>

    @PATCH(MEMBERS)
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): Response<BaseResponse<String>>

    @GET(MEMBERS)
    suspend fun getMyPage(): Response<BaseResponse<MyInfoResponse>>

    @GET(COMMENT + ALL)
    suspend fun getAllComment(): Response<BaseResponse<List<CommentResponse>>>

    @GET(COMMENT)
    suspend fun getMonthComment(@Query("date") date: String): Response<BaseResponse<List<CommentResponse>>>

    @PATCH(MEMBERS + PUSH)
    suspend fun patchCommentPushState(): Response<BaseResponse<Map<String, Char>>>

}