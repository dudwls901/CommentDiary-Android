package com.movingmaker.domain.repository

import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.request.ReportCommentModel
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.Diary

interface GatherDiaryRepository {

    suspend fun getMonthDiary(date: String): NetworkResult<BaseResponse<MutableList<Diary>>>

    suspend fun getAllDiary(): NetworkResult<BaseResponse<MutableList<Diary>>>

    suspend fun reportComment(reportCommentModel: ReportCommentModel): NetworkResult<BaseResponse<String>>

    suspend fun likeComment(commentId: Long): NetworkResult<BaseResponse<String>>
}