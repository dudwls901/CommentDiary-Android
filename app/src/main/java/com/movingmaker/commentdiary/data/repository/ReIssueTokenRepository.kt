package com.movingmaker.commentdiary.data.repository

import com.movingmaker.commentdiary.data.remote.RetrofitClient
import com.movingmaker.commentdiary.data.remote.response.LogInResponse
import retrofit2.Response

class ReIssueTokenRepository {

    companion object{
        val INSTANCE = ReIssueTokenRepository()
        val TAG = "레트로핏 로그"
    }

//    @Synchronized
    suspend fun reIssueToken(): Response<LogInResponse> {
        return RetrofitClient.reIssueTokenApiService.reIssueToken()
    }
}