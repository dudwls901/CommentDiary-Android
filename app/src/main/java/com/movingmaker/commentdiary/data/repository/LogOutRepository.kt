package com.movingmaker.commentdiary.data.repository

import com.movingmaker.commentdiary.data.remote.RetrofitClient
import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import retrofit2.Response

class LogOutRepository {

    companion object{
        val INSTANCE = LogOutRepository()
        val TAG = "레트로핏 로그"
    }
    suspend fun logOut(): Response<IsSuccessResponse> {
        return RetrofitClient.logOutApiService.logOut()
    }
}