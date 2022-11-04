package com.movingmaker.commentdiary.domain.repository

import com.movingmaker.commentdiary.data.model.Diary
import com.movingmaker.commentdiary.data.remote.request.ReportCommentRequest
import com.movingmaker.commentdiary.domain.model.BaseResponse
import com.movingmaker.commentdiary.domain.model.NetworkResult

interface GatherDiaryRepository {

    suspend fun getMonthDiary(date: String): NetworkResult<BaseResponse<MutableList<Diary>>>

    suspend fun getAllDiary(): NetworkResult<BaseResponse<MutableList<Diary>>>

    suspend fun reportComment(reportCommentRequest: ReportCommentRequest): NetworkResult<BaseResponse<String>>

    suspend fun likeComment(commentId: Long): NetworkResult<BaseResponse<String>>
}