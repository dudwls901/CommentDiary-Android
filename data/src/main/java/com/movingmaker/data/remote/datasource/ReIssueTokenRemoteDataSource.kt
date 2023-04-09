package com.movingmaker.data.remote.datasource

import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.response.AuthTokens
import com.movingmaker.domain.model.response.BaseResponse

interface ReIssueTokenRemoteDataSource {

    suspend fun reIssueToken(): NetworkResult<BaseResponse<AuthTokens>>

}