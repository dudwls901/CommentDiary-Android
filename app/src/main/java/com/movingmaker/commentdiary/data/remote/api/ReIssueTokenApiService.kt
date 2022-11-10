package com.movingmaker.commentdiary.data.remote.api

import com.movingmaker.commentdiary.data.model.AuthTokens
import com.movingmaker.commentdiary.data.util.REISSUE
import com.movingmaker.commentdiary.domain.model.BaseResponse
import retrofit2.Response
import retrofit2.http.POST

interface ReIssueTokenApiService {

    @POST(REISSUE)
    suspend fun reIssueToken(): Response<BaseResponse<AuthTokens>>

}