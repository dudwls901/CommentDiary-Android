package com.movingmaker.commentdiary.data.remote.api

import com.movingmaker.commentdiary.data.remote.response.LogInResponse
import com.movingmaker.commentdiary.data.util.REISSUE
import retrofit2.Response
import retrofit2.http.POST

interface ReIssueTokenApiService {

    @POST(REISSUE)
    suspend fun reIssueToken(): Response<LogInResponse>

}