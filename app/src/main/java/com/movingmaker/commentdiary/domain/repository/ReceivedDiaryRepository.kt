package com.movingmaker.commentdiary.domain.repository

import com.movingmaker.commentdiary.data.remote.request.ReportDiaryRequest
import com.movingmaker.commentdiary.data.remote.request.SaveCommentRequest
import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.data.remote.response.ReceivedDiaryResponse
import retrofit2.Response

interface ReceivedDiaryRepository {

    suspend fun getReceivedDiary(date: String): Response<ReceivedDiaryResponse>

    suspend fun saveComment(saveCommentRequest: SaveCommentRequest): Response<IsSuccessResponse>

    suspend fun reportDiary(reportDiaryRequest: ReportDiaryRequest): Response<IsSuccessResponse>

}