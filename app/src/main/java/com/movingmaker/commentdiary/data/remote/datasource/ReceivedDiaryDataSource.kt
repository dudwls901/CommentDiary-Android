package com.movingmaker.commentdiary.data.remote.datasource

import com.movingmaker.commentdiary.data.model.ReceivedDiary
import com.movingmaker.commentdiary.data.remote.request.ReportDiaryRequest
import com.movingmaker.commentdiary.data.remote.request.SaveCommentRequest
import com.movingmaker.commentdiary.domain.model.BaseResponse
import com.movingmaker.commentdiary.domain.model.NetworkResult

interface ReceivedDiaryDataSource {

    suspend fun getReceivedDiary(date: String): NetworkResult<BaseResponse<ReceivedDiary>>

    suspend fun saveComment(saveCommentRequest: SaveCommentRequest): NetworkResult<BaseResponse<String>>

    suspend fun reportDiary(reportDiaryRequest: ReportDiaryRequest): NetworkResult<BaseResponse<String>>

}