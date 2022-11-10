package com.movingmaker.commentdiary.data.repositoryimpl

import com.movingmaker.commentdiary.data.model.ReceivedDiary
import com.movingmaker.commentdiary.data.remote.datasource.ReceivedDiaryDataSource
import com.movingmaker.commentdiary.data.remote.request.ReportDiaryRequest
import com.movingmaker.commentdiary.data.remote.request.SaveCommentRequest
import com.movingmaker.commentdiary.domain.model.BaseResponse
import com.movingmaker.commentdiary.domain.model.NetworkResult
import com.movingmaker.commentdiary.domain.repository.ReceivedDiaryRepository
import javax.inject.Inject

class ReceivedDiaryRepositoryImpl @Inject constructor(
    private val receivedDiaryDataSource: ReceivedDiaryDataSource
) : ReceivedDiaryRepository {

    override suspend fun getReceivedDiary(date: String): NetworkResult<BaseResponse<ReceivedDiary>> =
        receivedDiaryDataSource.getReceivedDiary(date)

    override suspend fun saveComment(saveCommentRequest: SaveCommentRequest): NetworkResult<BaseResponse<String>> =
        receivedDiaryDataSource.saveComment(saveCommentRequest)

    override suspend fun reportDiary(reportDiaryRequest: ReportDiaryRequest): NetworkResult<BaseResponse<String>> =
        receivedDiaryDataSource.reportDiary(reportDiaryRequest)

}