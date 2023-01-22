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

    //    @Query("SELECT * FROM $COMMENT_DIARY_TABLE WHERE date like :date")
//    fun getPeriodDiaries(date: String): Flow<List<DiaryEntity>>)
    @Query("SELECT * FROM $COMMENT_DIARY_TABLE")
    fun getPeriodDiaries(): Flow<List<DiaryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTempDiary(commentDiaryEntity: DiaryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommentDiaries(vararg commentDiaryEntity: DiaryEntity)

    /**
     * 삭제된 행의 수 return
     * */
    @Delete
    suspend fun deleteTempCommentDiary(commentDiaryEntity: DiaryEntity): Int

    @Query("DELETE FROM $COMMENT_DIARY_TABLE WHERE userId in (:userIdList)")
    suspend fun clearCommentDiaries(userIdList: List<Long>)

}