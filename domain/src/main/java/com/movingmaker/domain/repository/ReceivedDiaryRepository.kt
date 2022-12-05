package com.movingmaker.domain.repository

import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.request.ReportDiaryModel
import com.movingmaker.domain.model.request.SaveCommentModel
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.ReceivedDiary

interface ReceivedDiaryRepository {

    suspend fun getReceivedDiary(date: String): NetworkResult<BaseResponse<ReceivedDiary>>

    suspend fun saveComment(saveCommentModel: SaveCommentModel): NetworkResult<BaseResponse<String>>

    suspend fun reportDiary(reportDiaryModel: ReportDiaryModel): NetworkResult<BaseResponse<String>>

}