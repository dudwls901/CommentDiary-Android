package com.movingmaker.data.remote.datasourceimpl

import com.movingmaker.data.remote.api.GatherDiaryApiService
import com.movingmaker.data.remote.datasource.GatherDiaryDataSource
import com.movingmaker.data.remote.model.request.ReportCommentRequest
import com.movingmaker.data.util.safeApiCall
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.Diary
import javax.inject.Inject

class GatherDiaryDataSourceImpl @Inject constructor(
    private val gatherDiaryApiService: GatherDiaryApiService
) : GatherDiaryDataSource {

    override suspend fun getMonthDiary(date: String): NetworkResult<BaseResponse<MutableList<Diary>>> =
        safeApiCall { gatherDiaryApiService.getMonthDiary(date) }

    override suspend fun getAllDiary(): NetworkResult<BaseResponse<MutableList<Diary>>> =
        safeApiCall { gatherDiaryApiService.getAllDiary() }

    override suspend fun reportComment(reportCommentRequest: ReportCommentRequest): NetworkResult<BaseResponse<String>> =
        safeApiCall { gatherDiaryApiService.reportComment(reportCommentRequest) }

    override suspend fun likeComment(commentId: Long): NetworkResult<BaseResponse<String>> =
        safeApiCall { gatherDiaryApiService.likeComment(commentId) }

}