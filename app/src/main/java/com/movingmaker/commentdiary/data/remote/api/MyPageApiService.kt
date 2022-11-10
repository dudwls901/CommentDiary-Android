package com.movingmaker.commentdiary.data.remote.api

import com.movingmaker.commentdiary.data.model.Comment
import com.movingmaker.commentdiary.data.model.MyInfo
import com.movingmaker.commentdiary.data.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.data.util.ALL
import com.movingmaker.commentdiary.data.util.COMMENT
import com.movingmaker.commentdiary.data.util.LOG_OUT
import com.movingmaker.commentdiary.data.util.MEMBERS
import com.movingmaker.commentdiary.data.util.PUSH
import com.movingmaker.commentdiary.domain.model.BaseResponse
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
    suspend fun getMyPage(): Response<BaseResponse<MyInfo>>

    @GET(COMMENT + ALL)
    suspend fun getAllComment(): Response<BaseResponse<List<Comment>>>

    @GET(COMMENT)
    suspend fun getMonthComment(@Query("date") date: String): Response<BaseResponse<List<Comment>>>

    @PATCH(MEMBERS + PUSH)
    suspend fun patchCommentPushState(): Response<BaseResponse<Map<String, Char>>>

    @DELETE(MEMBERS + LOG_OUT)
    suspend fun logOut(): Response<BaseResponse<String>>

}