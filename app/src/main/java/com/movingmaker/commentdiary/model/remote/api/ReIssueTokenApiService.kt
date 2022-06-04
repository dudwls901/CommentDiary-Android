package com.movingmaker.commentdiary.model.remote.api

import com.movingmaker.commentdiary.model.remote.response.LogInResponse
import com.movingmaker.commentdiary.util.Url.REISSUE
import retrofit2.Response
import retrofit2.http.POST

interface ReIssueTokenApiService {

    @POST(REISSUE)
    suspend fun reIssueToken(): Response<LogInResponse>

}