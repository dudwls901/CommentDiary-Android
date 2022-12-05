package com.movingmaker.data.repositoryimpl

import com.movingmaker.data.remote.datasource.GatherDiaryDataSource
import com.movingmaker.data.remote.model.request.toDataModel
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.request.ReportCommentModel
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.Diary
import com.movingmaker.domain.repository.GatherDiaryRepository
import javax.inject.Inject

class GatherDiaryRepositoryImpl @Inject constructor(
    private val gatherDiaryDataSource: GatherDiaryDataSource
) : GatherDiaryRepository {

    override suspend fun getMonthDiary(date: String): NetworkResult<BaseResponse<MutableList<Diary>>> =
        gatherDiaryDataSource.getMonthDiary(date)


    override suspend fun getAllDiary(): NetworkResult<BaseResponse<MutableList<Diary>>> =
        gatherDiaryDataSource.getAllDiary()


    override suspend fun reportComment(reportCommentModel: ReportCommentModel): NetworkResult<BaseResponse<String>> =
        gatherDiaryDataSource.reportComment(reportCommentModel.toDataModel())


    override suspend fun likeComment(commentId: Long): NetworkResult<BaseResponse<String>> =
        gatherDiaryDataSource.likeComment(commentId)


}