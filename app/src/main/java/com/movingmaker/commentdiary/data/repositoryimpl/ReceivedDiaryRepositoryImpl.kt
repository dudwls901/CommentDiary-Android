package com.movingmaker.commentdiary.data.repositoryimpl

import com.movingmaker.commentdiary.data.remote.datasource.ReceivedDiaryDataSource
import com.movingmaker.commentdiary.data.remote.request.ReportDiaryRequest
import com.movingmaker.commentdiary.data.remote.request.SaveCommentRequest
import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.data.remote.response.ReceivedDiaryResponse
import com.movingmaker.commentdiary.domain.repository.ReceivedDiaryRepository
import retrofit2.Response
import javax.inject.Inject

class ReceivedDiaryRepositoryImpl @Inject constructor(
    private val receivedDiaryDataSource: ReceivedDiaryDataSource
): ReceivedDiaryRepository {

    override suspend fun getReceivedDiary(date: String): Response<ReceivedDiaryResponse> {
        return receivedDiaryDataSource.getReceivedDiary(date)
    }

    override suspend fun saveComment(saveCommentRequest: SaveCommentRequest): Response<IsSuccessResponse> {
        return receivedDiaryDataSource.saveComment(saveCommentRequest)
    }

    override suspend fun reportDiary(reportDiaryRequest: ReportDiaryRequest): Response<IsSuccessResponse> {
        return receivedDiaryDataSource.reportDiary(reportDiaryRequest)
    }

}