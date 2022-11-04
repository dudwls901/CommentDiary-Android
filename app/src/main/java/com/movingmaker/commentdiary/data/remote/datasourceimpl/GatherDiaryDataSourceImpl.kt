package com.movingmaker.commentdiary.data.remote.datasourceimpl

import com.movingmaker.commentdiary.data.model.Diary
import com.movingmaker.commentdiary.data.remote.api.GatherDiaryApiService
import com.movingmaker.commentdiary.data.remote.datasource.GatherDiaryDataSource
import com.movingmaker.commentdiary.data.remote.request.ReportCommentRequest
import com.movingmaker.commentdiary.data.util.safeApiCall
import com.movingmaker.commentdiary.domain.model.BaseResponse
import com.movingmaker.commentdiary.domain.model.NetworkResult
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