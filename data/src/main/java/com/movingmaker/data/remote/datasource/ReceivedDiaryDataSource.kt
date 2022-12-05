package com.movingmaker.data.remote.datasource

import com.movingmaker.data.remote.model.request.ReportDiaryRequest
import com.movingmaker.data.remote.model.request.SaveCommentRequest
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.ReceivedDiary

interface ReceivedDiaryDataSource {

    suspend fun getReceivedDiary(date: String): NetworkResult<BaseResponse<ReceivedDiary>>

    suspend fun saveComment(saveCommentRequest: SaveCommentRequest): NetworkResult<BaseResponse<String>>

    suspend fun reportDiary(reportDiaryRequest: ReportDiaryRequest): NetworkResult<BaseResponse<String>>

}