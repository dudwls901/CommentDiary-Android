package com.movingmaker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.movingmaker.data.local.dao.DiaryDao
import com.movingmaker.data.local.dto.CommentListTypeConverter
import com.movingmaker.data.local.dto.DiaryEntity

@Database(
    entities = [DiaryEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(CommentListTypeConverter::class)
abstract class DiaryDatabase : RoomDatabase() {
    abstract fun diaryDao(): DiaryDao
}