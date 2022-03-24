package com.movingmaker.commentdiary.model.repository

import android.util.Log
import com.movingmaker.commentdiary.model.remote.RetrofitClient
import com.movingmaker.commentdiary.model.remote.response.LogInResponse
import retrofit2.Response

class ReIssueTokenRepository {

    companion object{
        val INSTANCE = ReIssueTokenRepository()
        val TAG = "레트로핏 로그"
    }

    suspend fun reIssueToken(): Response<LogInResponse> {
        return RetrofitClient.reIssueTokenApiService.reIssueToken()
    }
}