package com.movingmaker.commentdiary.data.repositoryimpl

import com.movingmaker.commentdiary.data.model.Diary
import com.movingmaker.commentdiary.data.remote.datasource.GatherDiaryDataSource
import com.movingmaker.commentdiary.data.remote.request.ReportCommentRequest
import com.movingmaker.commentdiary.domain.model.BaseResponse
import com.movingmaker.commentdiary.domain.model.NetworkResult
import com.movingmaker.commentdiary.domain.repository.GatherDiaryRepository
import javax.inject.Inject

class GatherDiaryRepositoryImpl @Inject constructor(
    private val gatherDiaryDataSource: GatherDiaryDataSource
) : GatherDiaryRepository {

    override suspend fun getMonthDiary(date: String): NetworkResult<BaseResponse<MutableList<Diary>>> =
        gatherDiaryDataSource.getMonthDiary(date)


    override suspend fun getAllDiary(): NetworkResult<BaseResponse<MutableList<Diary>>> =
        gatherDiaryDataSource.getAllDiary()


    override suspend fun reportComment(reportCommentRequest: ReportCommentRequest): NetworkResult<BaseResponse<String>> =
        gatherDiaryDataSource.reportComment(reportCommentRequest)


    override suspend fun likeComment(commentId: Long): NetworkResult<BaseResponse<String>> =
        gatherDiaryDataSource.likeComment(commentId)


}