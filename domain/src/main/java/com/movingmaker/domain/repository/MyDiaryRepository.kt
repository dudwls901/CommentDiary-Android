package com.movingmaker.domain.repository

import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.request.EditDiaryModel
import com.movingmaker.domain.model.request.SaveDiaryModel
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.DiaryId

interface MyDiaryRepository {

    suspend fun saveDiary(saveDiaryModel: SaveDiaryModel): NetworkResult<BaseResponse<DiaryId>>

    suspend fun editDiary(
        diaryId: Long,
        editDiaryModel: EditDiaryModel
    ): NetworkResult<BaseResponse<String>>

    suspend fun deleteDiary(diaryId: Long): NetworkResult<BaseResponse<String>>
}