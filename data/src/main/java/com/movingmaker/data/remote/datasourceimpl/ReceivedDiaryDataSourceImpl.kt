package com.movingmaker.data.remote.datasourceimpl

import com.movingmaker.data.remote.api.ReceivedDiaryApiService
import com.movingmaker.data.remote.datasource.ReceivedDiaryDataSource
import com.movingmaker.data.remote.model.request.ReportDiaryRequest
import com.movingmaker.data.remote.model.request.SaveCommentRequest
import com.movingmaker.data.util.safeApiCall
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.ReceivedDiary
import javax.inject.Inject

class ReceivedDiaryDataSourceImpl @Inject constructor(
    private val receivedDiaryApiService: ReceivedDiaryApiService
) : ReceivedDiaryDataSource {

    override suspend fun getReceivedDiary(date: String): NetworkResult<BaseResponse<ReceivedDiary>> =
        safeApiCall { receivedDiaryApiService.getReceivedDiary(date) }

    override suspend fun saveComment(saveCommentRequest: SaveCommentRequest): NetworkResult<BaseResponse<String>> =
        safeApiCall { receivedDiaryApiService.saveComment(saveCommentRequest) }

    override suspend fun reportDiary(reportDiaryRequest: ReportDiaryRequest): NetworkResult<BaseResponse<String>> =
        safeApiCall { receivedDiaryApiService.reportDiary(reportDiaryRequest) }

}