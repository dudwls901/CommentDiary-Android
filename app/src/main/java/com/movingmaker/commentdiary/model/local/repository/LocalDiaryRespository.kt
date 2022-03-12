package com.movingmaker.commentdiary.model.local.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.movingmaker.commentdiary.model.local.CommentDiaryDatabase
import com.movingmaker.commentdiary.model.local.DiaryDao
import com.movingmaker.commentdiary.model.local.entity.LocalDiary

class LocalDiaryRespository(application: Application) {
    private val diaryDao: DiaryDao

    init{
        var db: CommentDiaryDatabase = CommentDiaryDatabase.getInstance(application)!!
        diaryDao = db.diaryDao()
    }

    fun insert(localDiary: LocalDiary){
        diaryDao.insert(localDiary)
    }

    fun delete(localDiary: LocalDiary){
        diaryDao.delete(localDiary)
    }
    fun update(localDiary: LocalDiary){
        diaryDao.update(localDiary)
    }

    fun getAll(): List<LocalDiary>{
        return diaryDao.getAll()
    }
}