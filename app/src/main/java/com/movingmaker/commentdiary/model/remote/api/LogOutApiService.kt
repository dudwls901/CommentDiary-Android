package com.movingmaker.commentdiary.model.remote.api

import com.movingmaker.commentdiary.model.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.util.Url.LOG_OUT
import com.movingmaker.commentdiary.util.Url.MEMBERS
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PATCH


interface LogOutApiService {
    @DELETE(MEMBERS + LOG_OUT)
    suspend fun logOut(): Response<IsSuccessResponse>
}