package com.movingmaker.commentdiary.data.remote.datasource

import com.movingmaker.commentdiary.data.remote.request.EditDiaryRequest
import com.movingmaker.commentdiary.data.remote.request.SaveDiaryRequest
import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.data.remote.response.SaveDiaryResponse
import retrofit2.Response

interface MyDiaryDataSource {

    suspend fun saveDiary(saveDiaryRequest: SaveDiaryRequest): Response<SaveDiaryResponse>

    suspend fun editDiary(
        diaryId: Long,
        editDiaryRequest: EditDiaryRequest
    ): Response<IsSuccessResponse>

    suspend fun deleteDiary(diaryId: Long): Response<IsSuccessResponse>
}