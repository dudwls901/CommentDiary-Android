package com.movingmaker.commentdiary.model.repository

import android.util.Log
import com.movingmaker.commentdiary.model.remote.RetrofitClient
import com.movingmaker.commentdiary.model.remote.response.AuthTokenResponse
import com.movingmaker.commentdiary.model.remote.response.DiaryListResponse
import com.movingmaker.commentdiary.model.remote.response.LogInResponse
import retrofit2.Response

class ReIssueTokenRepository {

    companion object{
        val INSTANCE = ReIssueTokenRepository()
        val TAG = "레트로핏 로그"
    }

    suspend fun reIssueToken(): Response<LogInResponse> {
        Log.d(TAG, "reIssueToken: 불린겨 뭐여?????????????")
        return RetrofitClient.reIssueTokenApiService.reIssueToken()
    }
}