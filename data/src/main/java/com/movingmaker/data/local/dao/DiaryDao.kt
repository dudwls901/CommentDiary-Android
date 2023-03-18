package com.movingmaker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.movingmaker.data.local.dto.DiaryEntity
import com.movingmaker.data.util.DIARY_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {

    @Query("SELECT * FROM $DIARY_TABLE WHERE date like :date ORDER BY date DESC")
    fun getPeriodDiariesFlow(date: String): Flow<List<DiaryEntity>>

    @Query("SELECT * FROM $DIARY_TABLE WHERE date like :date ORDER BY date DESC")
    suspend fun getPeriodDiaries(date: String): List<DiaryEntity>

//    @Query("SELECT * FROM $COMMENT_DIARY_TABLE ORDER BY date DESC")
//    fun getAllDiaries(): Flow<List<DiaryEntity>>

    @Query("SELECT * FROM $DIARY_TABLE ORDER BY date DESC")
    suspend fun getAllDiaries(): List<DiaryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTempDiary(commentDiaryEntity: DiaryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cachingDiaries(vararg commentDiaryEntity: DiaryEntity)

    /**
     * 삭제된 행의 수 return
     * */
    @Query("DELETE FROM $DIARY_TABLE WHERE userId = :userId AND date = :date")
    suspend fun deleteDiary(
        userId: Long,
        date: String
    ): Int

    @Query("DELETE FROM $DIARY_TABLE WHERE userId in (:userIdList)")
    suspend fun clearCachedDiaries(userIdList: List<Long>)

}