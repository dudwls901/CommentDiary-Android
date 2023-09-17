package com.movingmaker.data.remote.api

import com.movingmaker.data.remote.model.response.BaseResponse
import com.movingmaker.data.remote.model.response.MyInfoResponse
import com.movingmaker.data.util.LOG_OUT
import com.movingmaker.data.util.MEMBERS
import com.movingmaker.data.util.PUSH
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH

interface MyPageApiService {

    //    todo Bearer, X-AUTH header 필요한지 확인
    @DELETE(LOG_OUT)
    suspend fun logOut(): Response<BaseResponse<String>>

    @PATCH(PUSH)
    suspend fun patchCommentPushState(): Response<BaseResponse<Map<String, Boolean>>>

    @DELETE(MEMBERS)
    suspend fun signOut(): Response<BaseResponse<String>>

    @GET(MEMBERS)
    suspend fun getMyPage(): Response<BaseResponse<MyInfoResponse>>
}