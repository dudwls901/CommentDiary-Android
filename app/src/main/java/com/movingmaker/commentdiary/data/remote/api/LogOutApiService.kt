package com.movingmaker.commentdiary.data.remote.api

import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.util.Url.LOG_OUT
import com.movingmaker.commentdiary.util.Url.MEMBERS
import retrofit2.Response
import retrofit2.http.DELETE


interface LogOutApiService {
    @DELETE(MEMBERS + LOG_OUT)
    suspend fun logOut(): Response<IsSuccessResponse>
}