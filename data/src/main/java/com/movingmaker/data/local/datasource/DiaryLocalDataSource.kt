package com.movingmaker.data.local.datasource

import com.movingmaker.data.local.dto.DiaryEntity
import com.movingmaker.domain.model.response.Diary

interface DiaryLocalDataSource {

    suspend fun getAllDiaries(): List<DiaryEntity>

    suspend fun gePeriodDiaries(date: String): List<DiaryEntity>

    suspend fun insertTempDiary(tempDiary: DiaryEntity)

    suspend fun cachingDiaries(vararg commentDiaryEntity: DiaryEntity)

    suspend fun deleteDiary(commentDiary: Diary): Int

    suspend fun clearCachedDiaries()
}