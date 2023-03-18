package com.movingmaker.data.remote.datasourceimpl

import com.movingmaker.data.remote.api.ReceivedDiaryApiService
import com.movingmaker.data.remote.datasource.ReceivedDiaryRemoteDataSource
import com.movingmaker.data.util.safeApiCall
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.ReceivedDiary
import javax.inject.Inject

class ReceivedDiaryRemoteDataSourceImpl @Inject constructor(
    private val receivedDiaryApiService: ReceivedDiaryApiService
) : ReceivedDiaryRemoteDataSource {

    override suspend fun getReceivedDiary(date: String): NetworkResult<BaseResponse<ReceivedDiary>> =
        safeApiCall { receivedDiaryApiService.getReceivedDiary(date) }
}