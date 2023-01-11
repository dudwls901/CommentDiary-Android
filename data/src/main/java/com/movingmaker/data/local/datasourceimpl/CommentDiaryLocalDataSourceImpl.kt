package com.movingmaker.data.local.datasourceimpl

import com.movingmaker.data.local.dao.CommentDiaryDao
import com.movingmaker.data.local.datasource.CommentDiaryLocalDataSource
import com.movingmaker.data.local.dto.DiaryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommentDiaryLocalDataSourceImpl @Inject constructor(
    private val commentDiaryDao: CommentDiaryDao,
) : CommentDiaryLocalDataSource {

    override fun getPeriodDiaries(date: String): Flow<List<DiaryEntity>> {
//        val ret = commentDiaryDao.getPeriodDiaries("%$date%")
        return commentDiaryDao.getPeriodDiaries()
    }

    override suspend fun insertTempDiary(tempDiary: DiaryEntity) {
        commentDiaryDao.insertTempDiary(tempDiary)
    }

    override suspend fun insertCommentDiaries(vararg commentDiaryEntity: DiaryEntity) {
        commentDiaryDao.insertCommentDiaries(*commentDiaryEntity)
    }

    override suspend fun deleteCommentDiary(commentDiaryEntity: DiaryEntity) {
        commentDiaryDao.deleteCommentDiary(commentDiaryEntity)
    }

    override suspend fun clearCommentDiaries() {
        commentDiaryDao.clearCommentDiaries()
    }
}