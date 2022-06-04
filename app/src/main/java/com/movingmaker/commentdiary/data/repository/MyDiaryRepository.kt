package com.movingmaker.commentdiary.data.repository

import com.movingmaker.commentdiary.data.remote.RetrofitClient
import com.movingmaker.commentdiary.data.remote.request.EditDiaryRequest
import com.movingmaker.commentdiary.data.remote.request.SaveDiaryRequest
import com.movingmaker.commentdiary.data.remote.response.DiaryListResponse
import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.data.remote.response.SaveDiaryResponse
import retrofit2.Response

class MyDiaryRepository {

    companion object{
        val INSTANCE = MyDiaryRepository()
        val TAG = "레트로핏 로그"
    }

    suspend fun getMonthDiary(date: String): Response<DiaryListResponse> {
        return RetrofitClient.myDiaryApiService.getMonthDiary(date)
    }

    suspend fun saveDiary(saveDiaryRequest: SaveDiaryRequest): Response<SaveDiaryResponse>{
        return RetrofitClient.myDiaryApiService.saveDiary(saveDiaryRequest)
    }

    suspend fun editDiary(diaryId: Long, editDiaryRequest: EditDiaryRequest): Response<IsSuccessResponse>{
        return RetrofitClient.myDiaryApiService.editDiary(diaryId,editDiaryRequest)
    }

    suspend fun deleteDiary(diaryId: Long): Response<IsSuccessResponse>{
        return RetrofitClient.myDiaryApiService.deleteDiary(diaryId)
    }
}