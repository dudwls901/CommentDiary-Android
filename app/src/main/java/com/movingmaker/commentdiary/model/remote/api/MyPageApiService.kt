package com.movingmaker.commentdiary.model.remote.api

import com.movingmaker.commentdiary.model.remote.Url
import com.movingmaker.commentdiary.model.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.model.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.model.remote.response.MyPageResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH


interface MyPageApiService {
//    @Headers("Authorization: Bearer ")
    @DELETE(Url.MEMBERS)
    suspend fun signOut(): Response<IsSuccessResponse>

    @PATCH(Url.MEMBERS)
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): Response<IsSuccessResponse>

    @GET(Url.MEMBERS)
    suspend fun getMyPage(): Response<MyPageResponse>

}