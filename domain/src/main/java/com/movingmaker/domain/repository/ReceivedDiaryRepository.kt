package com.movingmaker.domain.repository

import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.ReceivedDiary

interface ReceivedDiaryRepository {

    suspend fun getReceivedDiary(date: String): NetworkResult<BaseResponse<ReceivedDiary>>

}