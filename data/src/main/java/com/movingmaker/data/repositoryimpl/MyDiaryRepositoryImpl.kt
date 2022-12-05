package com.movingmaker.data.repositoryimpl

import com.movingmaker.data.remote.datasource.MyDiaryDataSource
import com.movingmaker.data.remote.model.request.toDataModel
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.request.EditDiaryModel
import com.movingmaker.domain.model.request.SaveDiaryModel
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.DiaryId
import com.movingmaker.domain.repository.MyDiaryRepository
import javax.inject.Inject

class MyDiaryRepositoryImpl @Inject constructor(
    private val myDiaryDataSource: MyDiaryDataSource
) : MyDiaryRepository {

    override suspend fun saveDiary(saveDiaryModel: SaveDiaryModel): NetworkResult<BaseResponse<DiaryId>> =
        myDiaryDataSource.saveDiary(saveDiaryModel.toDataModel())


    override suspend fun editDiary(
        diaryId: Long,
        editDiaryModel: EditDiaryModel
    ): NetworkResult<BaseResponse<String>> =
        myDiaryDataSource.editDiary(diaryId, editDiaryModel.toDataModel())


    override suspend fun deleteDiary(diaryId: Long): NetworkResult<BaseResponse<String>> =
        myDiaryDataSource.deleteDiary(diaryId)

}