package com.movingmaker.commentdiary.data.repositoryimpl

import com.movingmaker.commentdiary.data.model.DiaryId
import com.movingmaker.commentdiary.data.remote.datasource.MyDiaryDataSource
import com.movingmaker.commentdiary.data.remote.request.EditDiaryRequest
import com.movingmaker.commentdiary.data.remote.request.SaveDiaryRequest
import com.movingmaker.commentdiary.domain.model.BaseResponse
import com.movingmaker.commentdiary.domain.model.NetworkResult
import com.movingmaker.commentdiary.domain.repository.MyDiaryRepository
import javax.inject.Inject

class MyDiaryRepositoryImpl @Inject constructor(
    private val myDiaryDataSource: MyDiaryDataSource
) : MyDiaryRepository {

    override suspend fun saveDiary(saveDiaryRequest: SaveDiaryRequest): NetworkResult<BaseResponse<DiaryId>> =
        myDiaryDataSource.saveDiary(saveDiaryRequest)


    override suspend fun editDiary(
        diaryId: Long,
        editDiaryRequest: EditDiaryRequest
    ): NetworkResult<BaseResponse<String>> =
        myDiaryDataSource.editDiary(diaryId, editDiaryRequest)


    override suspend fun deleteDiary(diaryId: Long): NetworkResult<BaseResponse<String>> =
        myDiaryDataSource.deleteDiary(diaryId)

}