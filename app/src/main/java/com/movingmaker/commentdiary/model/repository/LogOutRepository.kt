package com.movingmaker.commentdiary.model.repository

import android.util.Log
import com.movingmaker.commentdiary.model.remote.RetrofitClient
import com.movingmaker.commentdiary.model.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.model.remote.response.IsSuccessResponse
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