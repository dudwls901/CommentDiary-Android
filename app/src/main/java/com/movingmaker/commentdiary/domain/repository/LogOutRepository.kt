package com.movingmaker.commentdiary.domain.repository

import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import retrofit2.Response

interface LogOutRepository {

    suspend fun logOut(): Response<IsSuccessResponse>
}