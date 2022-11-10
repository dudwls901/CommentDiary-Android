package com.movingmaker.commentdiary.data.remote.datasourceimpl

import com.movingmaker.commentdiary.data.model.DiaryId
import com.movingmaker.commentdiary.data.remote.api.MyDiaryApiService
import com.movingmaker.commentdiary.data.remote.datasource.MyDiaryDataSource
import com.movingmaker.commentdiary.data.remote.request.EditDiaryRequest
import com.movingmaker.commentdiary.data.remote.request.SaveDiaryRequest
import com.movingmaker.commentdiary.data.util.safeApiCall
import com.movingmaker.commentdiary.domain.model.BaseResponse
import com.movingmaker.commentdiary.domain.model.NetworkResult
import javax.inject.Inject

class MyDiaryDataSourceImpl @Inject constructor(
    private val myDiaryApiService: MyDiaryApiService
) : MyDiaryDataSource {

    override suspend fun saveDiary(saveDiaryRequest: SaveDiaryRequest): NetworkResult<BaseResponse<DiaryId>> =
        safeApiCall { myDiaryApiService.saveDiary(saveDiaryRequest) }


    override suspend fun editDiary(
        diaryId: Long,
        editDiaryRequest: EditDiaryRequest
    ): NetworkResult<BaseResponse<String>> =
        safeApiCall { myDiaryApiService.editDiary(diaryId, editDiaryRequest) }


    override suspend fun deleteDiary(diaryId: Long): NetworkResult<BaseResponse<String>> =
        safeApiCall { myDiaryApiService.deleteDiary(diaryId) }

}