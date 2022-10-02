package com.movingmaker.commentdiary.data.remote.datasourceimpl

import com.movingmaker.commentdiary.data.remote.api.ReceivedDiaryApiService
import com.movingmaker.commentdiary.data.remote.datasource.ReceivedDiaryDataSource
import com.movingmaker.commentdiary.data.remote.request.ReportDiaryRequest
import com.movingmaker.commentdiary.data.remote.request.SaveCommentRequest
import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.data.remote.response.ReceivedDiaryResponse
import retrofit2.Response
import javax.inject.Inject

class ReceivedDiaryDataSourceImpl @Inject constructor(
    private val receivedDiaryApiService: ReceivedDiaryApiService
): ReceivedDiaryDataSource {

    override suspend fun getReceivedDiary(date: String): Response<ReceivedDiaryResponse>{
        return receivedDiaryApiService.getReceivedDiary(date)
    }

    override suspend fun saveComment(saveCommentRequest: SaveCommentRequest): Response<IsSuccessResponse>{
        return receivedDiaryApiService.saveComment(saveCommentRequest)
    }

    override suspend fun reportDiary(reportDiaryRequest: ReportDiaryRequest): Response<IsSuccessResponse>{
        return receivedDiaryApiService.reportDiary(reportDiaryRequest)
    }

}