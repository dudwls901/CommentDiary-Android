package com.movingmaker.commentdiary.data.remote.datasourceimpl

import com.movingmaker.commentdiary.data.model.AuthTokens
import com.movingmaker.commentdiary.data.remote.api.ReIssueTokenApiService
import com.movingmaker.commentdiary.data.remote.datasource.ReIssueTokenDataSource
import com.movingmaker.commentdiary.data.util.safeApiCall
import com.movingmaker.commentdiary.domain.model.BaseResponse
import com.movingmaker.commentdiary.domain.model.NetworkResult
import javax.inject.Inject

class ReIssueTokenDataSourceImpl @Inject constructor(
    private val reIssueTokenApiService: ReIssueTokenApiService
) : ReIssueTokenDataSource {

    override suspend fun reIssueToken(): NetworkResult<BaseResponse<AuthTokens>> =
        safeApiCall { reIssueTokenApiService.reIssueToken() }

}