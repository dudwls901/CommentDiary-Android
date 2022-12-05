package com.movingmaker.data.repositoryimpl

import com.movingmaker.data.remote.datasource.ReceivedDiaryDataSource
import com.movingmaker.data.remote.model.request.toDataModel
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.request.ReportDiaryModel
import com.movingmaker.domain.model.request.SaveCommentModel
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.ReceivedDiary
import com.movingmaker.domain.repository.ReceivedDiaryRepository
import javax.inject.Inject

class ReceivedDiaryRepositoryImpl @Inject constructor(
    private val receivedDiaryDataSource: ReceivedDiaryDataSource
) : ReceivedDiaryRepository {

    override suspend fun getReceivedDiary(date: String): NetworkResult<BaseResponse<ReceivedDiary>> =
        receivedDiaryDataSource.getReceivedDiary(date)

    override suspend fun saveComment(saveCommentModel: SaveCommentModel): NetworkResult<BaseResponse<String>> =
        receivedDiaryDataSource.saveComment(saveCommentModel.toDataModel())

    override suspend fun reportDiary(reportDiaryModel: ReportDiaryModel): NetworkResult<BaseResponse<String>> =
        receivedDiaryDataSource.reportDiary(reportDiaryModel.toDataModel())

}