package com.movingmaker.data.local.datasourceimpl

import com.movingmaker.data.local.dao.DiaryDao
import com.movingmaker.data.local.datasource.DiaryLocalDataSource
import com.movingmaker.data.local.dto.DiaryEntity
import com.movingmaker.domain.model.response.Diary
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DiaryLocalDataSourceImpl @Inject constructor(
    private val diaryDao: DiaryDao,
) : DiaryLocalDataSource {

    override suspend fun getAllDiaries(): List<DiaryEntity> =
        diaryDao.getAllDiaries()

    override fun getAllDiariesFlow(): Flow<List<DiaryEntity>> =
        diaryDao.getAllDiariesFlow()

    override suspend fun getPeriodDiaries(date: String): List<DiaryEntity>  =
        diaryDao.getPeriodDiaries("%$date%")

    override fun getPeriodDiariesFlow(date: String): Flow<List<DiaryEntity>> =
        diaryDao.getPeriodDiariesFlow("%$date%")

    override suspend fun insertTempDiary(tempDiary: DiaryEntity) {
        diaryDao.insertTempDiary(tempDiary)
    }

    override suspend fun cachingDiaries(vararg commentDiaryEntity: DiaryEntity) =
        diaryDao.cachingDiaries(*commentDiaryEntity)

    override suspend fun deleteDiary(commentDiary: Diary): Int =
        diaryDao.deleteDiary(
            commentDiary.userId,
            commentDiary.date
        )

    override suspend fun clearCachedDiaries() {
        diaryDao.clearCachedDiaries(listOf(-1L))
    }
}