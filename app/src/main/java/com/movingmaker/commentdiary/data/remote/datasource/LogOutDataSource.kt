package com.movingmaker.commentdiary.data.remote.datasource

import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import retrofit2.Response

interface LogOutDataSource {

    suspend fun logOut(): Response<IsSuccessResponse>
}