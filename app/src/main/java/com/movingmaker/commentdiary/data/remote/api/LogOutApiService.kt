package com.movingmaker.commentdiary.data.remote.api

import com.movingmaker.commentdiary.data.util.LOG_OUT
import com.movingmaker.commentdiary.data.util.MEMBERS
import com.movingmaker.commentdiary.domain.model.BaseResponse
import retrofit2.Response
import retrofit2.http.DELETE

interface LogOutApiService {

    @DELETE(MEMBERS + LOG_OUT)
    suspend fun logOut(): Response<BaseResponse<String>>
}