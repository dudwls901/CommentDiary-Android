package com.movingmaker.commentdiary.data.remote.datasourceimpl

import com.movingmaker.commentdiary.data.model.ReceivedDiary
import com.movingmaker.commentdiary.data.remote.api.ReceivedDiaryApiService
import com.movingmaker.commentdiary.data.remote.datasource.ReceivedDiaryDataSource
import com.movingmaker.commentdiary.data.remote.request.ReportDiaryRequest
import com.movingmaker.commentdiary.data.remote.request.SaveCommentRequest
import com.movingmaker.commentdiary.data.util.safeApiCall
import com.movingmaker.commentdiary.domain.model.BaseResponse
import com.movingmaker.commentdiary.domain.model.NetworkResult
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