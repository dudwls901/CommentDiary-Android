package com.movingmaker.commentdiary.data.remote.datasource

import com.movingmaker.commentdiary.data.model.AuthTokens
import com.movingmaker.commentdiary.domain.model.BaseResponse
import com.movingmaker.commentdiary.domain.model.NetworkResult

interface ReIssueTokenDataSource {

    suspend fun reIssueToken(): NetworkResult<BaseResponse<AuthTokens>>

}