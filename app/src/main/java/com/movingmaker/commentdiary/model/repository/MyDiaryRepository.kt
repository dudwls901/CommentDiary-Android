package com.movingmaker.commentdiary.model.repository

import android.util.Log
import com.movingmaker.commentdiary.model.remote.RetrofitClient
import com.movingmaker.commentdiary.model.remote.request.EditDiaryRequest
import com.movingmaker.commentdiary.model.remote.request.SaveDiaryRequest
import com.movingmaker.commentdiary.model.remote.response.DiaryListResponse
import com.movingmaker.commentdiary.model.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.model.remote.response.SaveDiaryResponse
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

    suspend fun saveDiary(saveDiaryRequest: SaveDiaryRequest): Response<SaveDiaryResponse>{
        Log.d(TAG, "saveDiary: 불린겨 뭐여?????????????")
        return RetrofitClient.myDiaryApiService.saveDiary(saveDiaryRequest)
    }

    suspend fun editDiary(diaryId: Long, editDiaryRequest: EditDiaryRequest): Response<IsSuccessResponse>{
        Log.d(TAG, "editDiary: 불린겨 뭐여?????????????")
        return RetrofitClient.myDiaryApiService.editDiary(diaryId,editDiaryRequest)
    }

    suspend fun deleteDiary(diaryId: Long): Response<IsSuccessResponse>{
        Log.d(TAG, "editDiary: 불린겨 뭐여?????????????")
        return RetrofitClient.myDiaryApiService.deleteDiary(diaryId)
    }
}