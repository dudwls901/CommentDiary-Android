package com.movingmaker.domain.repository

import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.request.ReportCommentModel
import com.movingmaker.domain.model.request.ReportDiaryModel
import com.movingmaker.domain.model.response.BaseResponse

interface ReportRepository {

    suspend fun reportComment(reportCommentModel: ReportCommentModel): NetworkResult<BaseResponse<String>>

    suspend fun reportDiary(reportDiaryModel: ReportDiaryModel): NetworkResult<BaseResponse<String>>

}