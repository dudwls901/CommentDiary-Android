package com.movingmaker.commentdiary.data.remote.datasourceimpl

import com.movingmaker.commentdiary.data.remote.api.MyDiaryApiService
import com.movingmaker.commentdiary.data.remote.datasource.MyDiaryDataSource
import com.movingmaker.commentdiary.data.remote.request.EditDiaryRequest
import com.movingmaker.commentdiary.data.remote.request.SaveDiaryRequest
import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.data.remote.response.SaveDiaryResponse
import retrofit2.Response
import javax.inject.Inject

class MyDiaryDataSourceImpl @Inject constructor(
    private val myDiaryApiService: MyDiaryApiService
) : MyDiaryDataSource {

    override suspend fun saveDiary(saveDiaryRequest: SaveDiaryRequest): Response<SaveDiaryResponse> {
        return myDiaryApiService.saveDiary(saveDiaryRequest)
    }

    override suspend fun editDiary(
        diaryId: Long,
        editDiaryRequest: EditDiaryRequest
    ): Response<IsSuccessResponse> {
        return myDiaryApiService.editDiary(diaryId, editDiaryRequest)
    }

    override suspend fun deleteDiary(diaryId: Long): Response<IsSuccessResponse> {
        return myDiaryApiService.deleteDiary(diaryId)
    }
}