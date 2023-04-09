package com.movingmaker.commentdiary.di

import android.content.Context
import androidx.room.Room
import com.movingmaker.data.local.DiaryDatabase
import com.movingmaker.data.local.dao.DiaryDao
import com.movingmaker.data.util.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DiaryDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            DiaryDatabase::class.java,
            DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideDiaryDao(
        diaryDatabase: DiaryDatabase
    ): DiaryDao {
        return diaryDatabase.diaryDao()
    }
}