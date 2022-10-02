package com.movingmaker.commentdiary.data.repositoryimpl

import com.movingmaker.commentdiary.data.remote.datasource.GatherDiaryDataSource
import com.movingmaker.commentdiary.data.remote.request.ReportCommentRequest
import com.movingmaker.commentdiary.data.remote.response.DiaryListResponse
import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.domain.repository.GatherDiaryRepository
import retrofit2.Response
import javax.inject.Inject

class GatherDiaryRepositoryImpl @Inject constructor(
    private val gatherDiaryDataSource: GatherDiaryDataSource
) : GatherDiaryRepository {

    override suspend fun getMonthDiary(date: String): Response<DiaryListResponse> {
        return gatherDiaryDataSource.getMonthDiary(date)
    }

    override suspend fun getAllDiary(): Response<DiaryListResponse> {
        return gatherDiaryDataSource.getAllDiary()
    }

    override suspend fun reportComment(reportCommentRequest: ReportCommentRequest): Response<IsSuccessResponse> {
        return gatherDiaryDataSource.reportComment(reportCommentRequest)
    }

    override suspend fun likeComment(commentId: Long): Response<IsSuccessResponse> {
        return gatherDiaryDataSource.likeComment(commentId)
    }

}