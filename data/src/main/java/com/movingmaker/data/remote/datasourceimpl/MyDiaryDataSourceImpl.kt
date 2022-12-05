package com.movingmaker.data.remote.datasourceimpl

import com.movingmaker.data.remote.api.MyDiaryApiService
import com.movingmaker.data.remote.datasource.MyDiaryDataSource
import com.movingmaker.data.remote.model.request.EditDiaryRequest
import com.movingmaker.data.remote.model.request.SaveDiaryRequest
import com.movingmaker.data.util.safeApiCall
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.DiaryId
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