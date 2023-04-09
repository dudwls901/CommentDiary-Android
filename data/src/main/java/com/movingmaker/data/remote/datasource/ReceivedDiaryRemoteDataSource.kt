package com.movingmaker.data.remote.datasource

import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.ReceivedDiary

interface ReceivedDiaryRemoteDataSource {
    suspend fun getReceivedDiary(date: String): NetworkResult<BaseResponse<ReceivedDiary>>

}