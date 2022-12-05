package com.movingmaker.data.remote.api

import com.movingmaker.data.remote.model.response.BaseResponse
import com.movingmaker.data.util.LOG_OUT
import com.movingmaker.data.util.MEMBERS
import retrofit2.Response
import retrofit2.http.DELETE

interface LogOutApiService {

    @DELETE(MEMBERS + LOG_OUT)
    suspend fun logOut(): Response<BaseResponse<String>>
}