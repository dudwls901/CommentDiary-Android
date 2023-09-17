package com.movingmaker.data.remote.datasourceimpl

import com.movingmaker.data.remote.api.BearerApiService
import com.movingmaker.data.remote.datasource.ReportRemoteDataSource
import com.movingmaker.data.remote.model.request.ReportCommentRequest
import com.movingmaker.data.remote.model.request.ReportDiaryRequest
import com.movingmaker.data.util.safeApiCall
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.response.BaseResponse
import javax.inject.Inject

class ReportRemoteDataSourceImpl @Inject constructor(
    private val bearerApiService: BearerApiService
) : ReportRemoteDataSource {
    override suspend fun reportComment(reportCommentRequest: ReportCommentRequest): NetworkResult<BaseResponse<String>> =
        safeApiCall { bearerApiService.reportComment(reportCommentRequest) }

    override suspend fun reportDiary(reportDiaryRequest: ReportDiaryRequest): NetworkResult<BaseResponse<String>> =
        safeApiCall { bearerApiService.reportDiary(reportDiaryRequest) }
}