package com.movingmaker.data.local.datasource

import com.movingmaker.data.local.dto.DiaryEntity
import kotlinx.coroutines.flow.Flow

interface CommentDiaryLocalDataSource {
    fun getPeriodDiaries(date: String): Flow<List<DiaryEntity>>
    suspend fun insertCommentDiary(vararg commentDiaryEntity: DiaryEntity)
    suspend fun deleteCommentDiary(commentDiaryEntity: DiaryEntity)
}