package com.movingmaker.commentdiary.data.remote.datasource

import com.movingmaker.commentdiary.data.remote.request.EditDiaryRequest
import com.movingmaker.commentdiary.data.remote.request.SaveDiaryRequest
import com.movingmaker.commentdiary.domain.model.BaseResponse
import com.movingmaker.commentdiary.domain.model.NetworkResult

interface MyDiaryDataSource {

    suspend fun saveDiary(saveDiaryRequest: SaveDiaryRequest): NetworkResult<BaseResponse<Long>>

    suspend fun editDiary(
        diaryId: Long,
        editDiaryRequest: EditDiaryRequest
    ): NetworkResult<BaseResponse<String>>

    suspend fun deleteDiary(diaryId: Long): NetworkResult<BaseResponse<String>>
}