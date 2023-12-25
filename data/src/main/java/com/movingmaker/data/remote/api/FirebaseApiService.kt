package com.movingmaker.data.remote.api

import com.movingmaker.data.remote.model.response.BaseResponse
import com.movingmaker.data.util.REGISTER_USER
import com.movingmaker.domain.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FirebaseApiService {

    @POST(REGISTER_USER)
    suspend fun registerUser(@Body user: User): Response<BaseResponse<Unit>>
}