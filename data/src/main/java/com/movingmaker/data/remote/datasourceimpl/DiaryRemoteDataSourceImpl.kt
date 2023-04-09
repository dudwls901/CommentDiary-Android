package com.movingmaker.data.remote.datasourceimpl

import com.movingmaker.data.remote.api.DiaryApiService
import com.movingmaker.data.remote.datasource.DiaryRemoteDataSource
import com.movingmaker.data.remote.model.request.EditDiaryRequest
import com.movingmaker.data.remote.model.request.SaveDiaryRequest
import com.movingmaker.data.util.safeApiCall
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.Diary
import com.movingmaker.domain.model.response.DiaryId
import javax.inject.Inject

class DiaryRemoteDataSourceImpl @Inject constructor(
    private val diaryApiService: DiaryApiService
) : DiaryRemoteDataSource {

    override suspend fun saveDiary(saveDiaryRequest: SaveDiaryRequest): NetworkResult<BaseResponse<DiaryId>> =
        safeApiCall { diaryApiService.saveDiary(saveDiaryRequest) }

    override suspend fun editDiary(
        diaryId: Long,
        editDiaryRequest: EditDiaryRequest
    ): NetworkResult<BaseResponse<String>> =
        safeApiCall { diaryApiService.editDiary(diaryId, editDiaryRequest) }

    override suspend fun deleteDiary(diaryId: Long): NetworkResult<BaseResponse<String>> =
        safeApiCall { diaryApiService.deleteDiary(diaryId) }

    override suspend fun getAllDiaries(): NetworkResult<BaseResponse<List<Diary>>> =
        safeApiCall { diaryApiService.getAllDiaries() }

    override suspend fun getPeriodDiaries(date: String): NetworkResult<BaseResponse<List<Diary>>> =
        safeApiCall { diaryApiService.getPeriodDiaries(date) }

    override suspend fun getDiary(diaryId: Long): NetworkResult<BaseResponse<Diary>> =
        safeApiCall { diaryApiService.getDiary(diaryId) }
}