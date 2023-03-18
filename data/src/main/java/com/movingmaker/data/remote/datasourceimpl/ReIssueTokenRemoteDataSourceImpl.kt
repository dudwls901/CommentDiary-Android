package com.movingmaker.data.remote.datasourceimpl

import com.movingmaker.data.remote.api.ReIssueTokenApiService
import com.movingmaker.data.remote.datasource.ReIssueTokenRemoteDataSource
import com.movingmaker.data.util.safeApiCall
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.response.AuthTokens
import com.movingmaker.domain.model.response.BaseResponse
import javax.inject.Inject

class ReIssueTokenRemoteDataSourceImpl @Inject constructor(
    private val reIssueTokenApiService: ReIssueTokenApiService
) : ReIssueTokenRemoteDataSource {

    override suspend fun reIssueToken(): NetworkResult<BaseResponse<AuthTokens>> =
        safeApiCall { reIssueTokenApiService.reIssueToken() }
}