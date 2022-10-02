package com.movingmaker.commentdiary.data.remote.datasource

import com.movingmaker.commentdiary.data.remote.response.LogInResponse
import retrofit2.Response

interface ReIssueTokenDataSource {

    suspend fun reIssueToken(): Response<LogInResponse>

}