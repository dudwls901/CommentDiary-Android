package com.movingmaker.data.repositoryimpl

import com.movingmaker.data.local.datasource.DiaryLocalDataSource
import com.movingmaker.data.local.dto.toEntity
import com.movingmaker.data.remote.datasource.DiaryRemoteDataSource
import com.movingmaker.data.remote.model.request.toDataModel
import com.movingmaker.domain.model.ErrorType
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.request.EditDiaryModel
import com.movingmaker.domain.model.request.SaveDiaryModel
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.Diary
import com.movingmaker.domain.model.response.DiaryId
import com.movingmaker.domain.repository.DiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(
    private val diaryRemoteDataSource: DiaryRemoteDataSource,
    private val diaryLocalDataSource: DiaryLocalDataSource
) : DiaryRepository {

    override suspend fun saveDiary(saveDiaryModel: SaveDiaryModel): NetworkResult<BaseResponse<DiaryId>> =
        diaryRemoteDataSource.saveDiary(saveDiaryModel.toDataModel())

    override suspend fun editDiary(
        diaryId: Long,
        editDiaryModel: EditDiaryModel
    ): NetworkResult<BaseResponse<String>> =
        diaryRemoteDataSource.editDiary(diaryId, editDiaryModel.toDataModel())

    override suspend fun deleteDiary(diaryId: Long): NetworkResult<BaseResponse<String>> =
        diaryRemoteDataSource.deleteDiary(diaryId)

    override suspend fun updatePeriodDiaries(date: String): NetworkResult<BaseResponse<List<Diary>>> {
        return try {
            val remoteDiaries = diaryRemoteDataSource.getPeriodDiaries(date)
            when (remoteDiaries) {
                is NetworkResult.Success -> {
                    cachingDiaries(remoteDiaries.data.result)
                }
                else -> {
                    /*no op*/
                }
            }
            remoteDiaries
        } catch (e: Exception) {
            NetworkResult.Exception(ErrorType.UNKNOWN)
        }
    }

    override suspend fun updateAllDiaries(): NetworkResult<BaseResponse<List<Diary>>> {
        return try {
            val remoteDiaries = diaryRemoteDataSource.getAllDiaries()
            when (remoteDiaries) {
                is NetworkResult.Success -> {
//                    clearCachedDiaries()
                    cachingDiaries(remoteDiaries.data.result)
                }
                else -> {
                    /*no op*/
                }
            }
            remoteDiaries
        } catch (e: Exception) {
            NetworkResult.Exception(ErrorType.UNKNOWN)
        }
    }

    override suspend fun getAllDiaries(): List<Diary> =
        diaryLocalDataSource.getAllDiaries().map { it.toDomainModel() }

    override suspend fun getPeriodDiaries(date: String): List<Diary> =
        diaryLocalDataSource.getPeriodDiaries(date).map { it.toDomainModel() }

    override fun getPeriodDiariesFlow(date: String): Flow<List<Diary>> =
        diaryLocalDataSource.getPeriodDiariesFlow(date).map { diaryList ->
            diaryList.map {
                it.toDomainModel()
            }
        }
    /**
     * 임시 저장 일기 로컬에 넣기
     * */
    override suspend fun insertTempDiary(tempDiary: Diary) {
        diaryLocalDataSource.insertTempDiary(tempDiary.toEntity())
    }


    override suspend fun deleteTempDiary(commentDiary: Diary): Int =
        diaryLocalDataSource.deleteDiary(commentDiary)

    /**
     * 캐시된 일기 모두 삭제하기
     * */
    override suspend fun clearCachedDiaries() {
        diaryLocalDataSource.clearCachedDiaries()
    }

    /**
     * 리모트에서 받아온 일기 로컬에 넣기 (캐싱)
     * */
    private suspend fun cachingDiaries(commentDiaries: List<Diary>) {
        clearCachedDiaries()
        diaryLocalDataSource.cachingDiaries(*commentDiaries.map { it.toEntity() }
            .toTypedArray())
    }

}