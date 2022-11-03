package com.movingmaker.commentdiary.data.remote.api

import com.movingmaker.commentdiary.common.util.Url.ALL
import com.movingmaker.commentdiary.common.util.Url.COMMENT
import com.movingmaker.commentdiary.common.util.Url.LOG_OUT
import com.movingmaker.commentdiary.common.util.Url.MEMBERS
import com.movingmaker.commentdiary.common.util.Url.PUSH
import com.movingmaker.commentdiary.data.model.Comment
import com.movingmaker.commentdiary.data.model.MyInfo
import com.movingmaker.commentdiary.data.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.domain.model.BaseResponse
import retrofit2.Response
import retrofit2.http.*


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