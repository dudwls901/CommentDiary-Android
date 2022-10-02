package com.movingmaker.commentdiary.data.repositoryimpl

import com.movingmaker.commentdiary.data.remote.datasource.MyDiaryDataSource
import com.movingmaker.commentdiary.data.remote.request.EditDiaryRequest
import com.movingmaker.commentdiary.data.remote.request.SaveDiaryRequest
import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.data.remote.response.SaveDiaryResponse
import com.movingmaker.commentdiary.domain.repository.MyDiaryRepository
import retrofit2.Response
import javax.inject.Inject

class MyDiaryRepositoryImpl @Inject constructor(
    private val myDiaryDataSource: MyDiaryDataSource
) : MyDiaryRepository {

    override suspend fun saveDiary(saveDiaryRequest: SaveDiaryRequest): Response<SaveDiaryResponse> {
        return myDiaryDataSource.saveDiary(saveDiaryRequest)
    }

    override suspend fun editDiary(
        diaryId: Long,
        editDiaryRequest: EditDiaryRequest
    ): Response<IsSuccessResponse> {
        return myDiaryDataSource.editDiary(diaryId, editDiaryRequest)
    }

    override suspend fun deleteDiary(diaryId: Long): Response<IsSuccessResponse> {
        return myDiaryDataSource.deleteDiary(diaryId)
    }
}