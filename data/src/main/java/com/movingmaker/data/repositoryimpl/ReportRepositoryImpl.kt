package com.movingmaker.data.repositoryimpl

import com.movingmaker.data.remote.datasource.ReportRemoteDataSource
import com.movingmaker.data.remote.model.request.toDataModel
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.request.ReportCommentModel
import com.movingmaker.domain.model.request.ReportDiaryModel
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.repository.ReportRepository
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val reportRemoteDataSource: ReportRemoteDataSource
): ReportRepository {
    /**
     * 코멘트 신고하기
     * */
    override suspend fun reportComment(reportCommentModel: ReportCommentModel): NetworkResult<BaseResponse<String>> =
        reportRemoteDataSource.reportComment(reportCommentModel.toDataModel())

    override suspend fun reportDiary(reportDiaryModel: ReportDiaryModel): NetworkResult<BaseResponse<String>> =
        reportRemoteDataSource.reportDiary(reportDiaryModel.toDataModel())

}