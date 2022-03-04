package com.movingmaker.commentdiary.model.repository

import android.util.Log
import com.movingmaker.commentdiary.model.remote.RetrofitClient
import com.movingmaker.commentdiary.model.remote.response.DiaryListResponse
import retrofit2.Response

class MyDiaryRepository {

    companion object{
        val INSTANCE = MyDiaryRepository()
        val TAG = "레트로핏 로그"
    }

    suspend fun getMonthDiary(date: String): Response<DiaryListResponse> {
        Log.d(TAG, "getMonthDiary: 불린겨 뭐여?????????????")
        return RetrofitClient.myDiaryApiService.getMonthDiary(date)
    }
}