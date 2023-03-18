package com.movingmaker.data.remote.datasource

import com.movingmaker.data.remote.model.request.ReportCommentRequest
import com.movingmaker.data.remote.model.request.ReportDiaryRequest
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.response.BaseResponse

interface ReportRemoteDataSource {

    suspend fun reportComment(reportCommentRequest: ReportCommentRequest): NetworkResult<BaseResponse<String>>

    suspend fun reportDiary(reportDiaryRequest: ReportDiaryRequest): NetworkResult<BaseResponse<String>>

}