package com.movingmaker.commentdiary.data.remote.datasourceimpl

import com.movingmaker.commentdiary.data.remote.api.GatherDiaryApiService
import com.movingmaker.commentdiary.data.remote.datasource.GatherDiaryDataSource
import com.movingmaker.commentdiary.data.remote.request.ReportCommentRequest
import com.movingmaker.commentdiary.data.remote.response.DiaryListResponse
import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import retrofit2.Response
import javax.inject.Inject

class GatherDiaryDataSourceImpl @Inject constructor(
    private val gatherDiaryApiService: GatherDiaryApiService
): GatherDiaryDataSource {

    override suspend fun getMonthDiary(date: String): Response<DiaryListResponse> {
        return gatherDiaryApiService.getMonthDiary(date)
    }

    override suspend fun getAllDiary(): Response<DiaryListResponse>{
        return gatherDiaryApiService.getAllDiary()
    }

    override suspend fun reportComment(reportCommentRequest: ReportCommentRequest): Response<IsSuccessResponse>{
        return gatherDiaryApiService.reportComment(reportCommentRequest)
    }

    override suspend fun likeComment(commentId: Long): Response<IsSuccessResponse>{
        return gatherDiaryApiService.likeComment(commentId)
    }

}