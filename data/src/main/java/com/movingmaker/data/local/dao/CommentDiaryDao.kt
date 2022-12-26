package com.movingmaker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.movingmaker.data.local.dto.DiaryEntity
import com.movingmaker.data.util.COMMENT_DIARY_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDiaryDao {

    @Query("SELECT * FROM $COMMENT_DIARY_TABLE")
    fun getPeriodDiaries(date: String): Flow<List<DiaryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommentDiary(vararg commentDiaryEntity: DiaryEntity)

    @Delete
    suspend fun deleteCommentDiary(commentDiaryEntity: DiaryEntity)
}