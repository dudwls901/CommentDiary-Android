package com.movingmaker.data.remote.datasource

import com.movingmaker.data.remote.model.request.ReportCommentRequest
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.Diary

interface GatherDiaryDataSource {

    suspend fun getMonthDiary(date: String): NetworkResult<BaseResponse<MutableList<Diary>>>

    suspend fun getAllDiary(): NetworkResult<BaseResponse<MutableList<Diary>>>

    suspend fun reportComment(reportCommentRequest: ReportCommentRequest): NetworkResult<BaseResponse<String>>

    suspend fun likeComment(commentId: Long): NetworkResult<BaseResponse<String>>

}