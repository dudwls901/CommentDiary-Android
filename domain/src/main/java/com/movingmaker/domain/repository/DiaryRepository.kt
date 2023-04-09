package com.movingmaker.domain.repository

import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.request.EditDiaryModel
import com.movingmaker.domain.model.request.SaveDiaryModel
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.Diary
import com.movingmaker.domain.model.response.DiaryId
import kotlinx.coroutines.flow.Flow

interface DiaryRepository {

    suspend fun saveDiary(saveDiaryModel: SaveDiaryModel): NetworkResult<BaseResponse<DiaryId>>

    suspend fun editDiary(
        diaryId: Long,
        editDiaryModel: EditDiaryModel
    ): NetworkResult<BaseResponse<String>>

    suspend fun deleteDiary(diaryId: Long): NetworkResult<BaseResponse<String>>

    suspend fun updatePeriodDiaries(date: String): NetworkResult<BaseResponse<List<Diary>>>

    suspend fun updateAllDiaries(): NetworkResult<BaseResponse<List<Diary>>>

    suspend fun getAllDiaries(): List<Diary>

    fun getAllDiariesFlow(): Flow<List<Diary>>

    suspend fun getPeriodDiaries(date: String): List<Diary>

    fun getPeriodDiariesFlow(date: String): Flow<List<Diary>>

    suspend fun insertTempDiary(tempDiary: Diary)

    suspend fun deleteTempDiary(commentDiary: Diary): Int

//    suspend fun cachingCommentDiaries(commentDiaries: List<Diary>)

    suspend fun clearCachedDiaries()

}