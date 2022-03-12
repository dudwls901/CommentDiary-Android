package com.movingmaker.commentdiary.model.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.movingmaker.commentdiary.model.local.entity.LocalDiary

@Dao
interface DiaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(localDiary: LocalDiary)

    @Update
    fun update(localDiary: LocalDiary)

    @Delete
    fun delete(localDiary: LocalDiary)

    @Query("SELECT * FROM LocalDiary")
    fun getAll(): List<LocalDiary>


}