package com.movingmaker.data.remote.datasource

import com.movingmaker.data.remote.model.request.EditDiaryRequest
import com.movingmaker.data.remote.model.request.SaveDiaryRequest
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.DiaryId

interface MyDiaryDataSource {

    suspend fun saveDiary(saveDiaryRequest: SaveDiaryRequest): NetworkResult<BaseResponse<DiaryId>>

    suspend fun editDiary(
        diaryId: Long,
        editDiaryRequest: EditDiaryRequest
    ): NetworkResult<BaseResponse<String>>

    suspend fun deleteDiary(diaryId: Long): NetworkResult<BaseResponse<String>>
}