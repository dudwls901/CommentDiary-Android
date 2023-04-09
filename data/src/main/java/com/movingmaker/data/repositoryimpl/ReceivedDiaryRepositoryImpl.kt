package com.movingmaker.data.repositoryimpl

import com.movingmaker.data.remote.datasource.ReceivedDiaryRemoteDataSource
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.ReceivedDiary
import com.movingmaker.domain.repository.ReceivedDiaryRepository
import javax.inject.Inject

class ReceivedDiaryRepositoryImpl @Inject constructor(
    private val receivedDiaryRemoteDataSource: ReceivedDiaryRemoteDataSource
) : ReceivedDiaryRepository {

    override suspend fun getReceivedDiary(date: String): NetworkResult<BaseResponse<ReceivedDiary>> =
        receivedDiaryRemoteDataSource.getReceivedDiary(date)

}