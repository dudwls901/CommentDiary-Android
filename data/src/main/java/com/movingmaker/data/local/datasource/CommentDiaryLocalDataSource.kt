package com.movingmaker.data.local.datasource

import com.movingmaker.data.local.dto.DiaryEntity
import kotlinx.coroutines.flow.Flow

interface CommentDiaryLocalDataSource {
    fun getPeriodDiaries(date: String): Flow<List<DiaryEntity>>
    suspend fun insertTempDiary(tempDiary: DiaryEntity)
    suspend fun insertCommentDiaries(vararg commentDiaryEntity: DiaryEntity)
    suspend fun deleteCommentDiary(commentDiaryEntity: DiaryEntity)
    suspend fun clearCommentDiaries()
}