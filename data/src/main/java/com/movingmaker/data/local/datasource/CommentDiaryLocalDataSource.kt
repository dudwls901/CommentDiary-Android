package com.movingmaker.data.local.datasource

import com.movingmaker.data.local.dto.DiaryEntity
import com.movingmaker.domain.model.response.Diary
import kotlinx.coroutines.flow.Flow

interface CommentDiaryLocalDataSource {
    fun getPeriodDiaries(date: String): Flow<List<DiaryEntity>>
    suspend fun insertTempDiary(tempDiary: DiaryEntity)
    suspend fun insertCommentDiaries(vararg commentDiaryEntity: DiaryEntity)
    suspend fun deleteTempCommentDiary(commentDiary: Diary): Int
    suspend fun clearCommentDiaries()
}