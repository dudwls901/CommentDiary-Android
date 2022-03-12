package com.movingmaker.commentdiary.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.movingmaker.commentdiary.model.local.entity.LocalDiary

@Database(entities = [LocalDiary::class], version = 1)
abstract class CommentDiaryDatabase : RoomDatabase(){
//    abstract fun historyDAO(): HistoryDAO
    abstract fun diaryDao(): DiaryDao



    companion object{
        private var INSTANCE : CommentDiaryDatabase? =null
//        private val migration_1_2 = object : Migration(1,2){
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("CREATE TABLE `DIARY` (`id` INTEGER, `review` TEXT," + "PRIMARY KEY(`id`))")
//            }
//        }

        fun getInstance(context : Context) : CommentDiaryDatabase?{
//            if(INSTANCE ==null){
                synchronized(CommentDiaryDatabase::class){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        CommentDiaryDatabase::class.java,
                        "LocalDiaryDB"
                    )
                        .fallbackToDestructiveMigration()
//                        .addMigrations(migration_1_2)
                        .build()
                }
//            }
            return INSTANCE
        }
    }
}