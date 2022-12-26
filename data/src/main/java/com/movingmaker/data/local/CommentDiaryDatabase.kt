package com.movingmaker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.movingmaker.data.local.dao.CommentDiaryDao
import com.movingmaker.data.local.dto.DiaryEntity

@Database(
    entities = [DiaryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CommentDiaryDatabase : RoomDatabase() {
    abstract fun commentDiaryDao(): CommentDiaryDao
}