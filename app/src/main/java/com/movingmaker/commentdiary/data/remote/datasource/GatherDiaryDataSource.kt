package com.movingmaker.commentdiary.data.remote.datasource

import com.movingmaker.commentdiary.data.remote.request.ReportCommentRequest
import com.movingmaker.commentdiary.data.remote.response.DiaryListResponse
import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import retrofit2.Response

interface GatherDiaryDataSource {

    suspend fun getMonthDiary(date: String): Response<DiaryListResponse>

    suspend fun getAllDiary(): Response<DiaryListResponse>

    suspend fun reportComment(reportCommentRequest: ReportCommentRequest): Response<IsSuccessResponse>

    suspend fun likeComment(commentId: Long): Response<IsSuccessResponse>

}