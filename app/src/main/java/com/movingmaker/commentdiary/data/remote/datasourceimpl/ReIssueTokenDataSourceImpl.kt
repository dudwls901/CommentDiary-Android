package com.movingmaker.commentdiary.data.remote.datasourceimpl

import com.movingmaker.commentdiary.data.remote.api.ReIssueTokenApiService
import com.movingmaker.commentdiary.data.remote.datasource.ReIssueTokenDataSource
import com.movingmaker.commentdiary.data.remote.response.LogInResponse
import retrofit2.Response
import javax.inject.Inject

class ReIssueTokenDataSourceImpl @Inject constructor(
    private val reIssueTokenApiService: ReIssueTokenApiService
): ReIssueTokenDataSource{

    override suspend fun reIssueToken(): Response<LogInResponse> {
        return reIssueTokenApiService.reIssueToken()
    }
}