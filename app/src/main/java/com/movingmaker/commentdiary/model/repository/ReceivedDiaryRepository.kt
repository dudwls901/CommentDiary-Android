package com.movingmaker.commentdiary.model.repository

import android.util.Log
import com.movingmaker.commentdiary.model.remote.RetrofitClient
import com.movingmaker.commentdiary.model.remote.request.*
import com.movingmaker.commentdiary.model.remote.response.*
import retrofit2.Response

class ReceivedDiaryRepository {

    companion object{
        val INSTANCE = ReceivedDiaryRepository()
        val TAG = "레트로핏 로그"
    }

    suspend fun getReceivedDiary(date: String): Response<ReceivedDiaryResponse>{
        Log.d(TAG, "getReceived: 불린겨 뭐여?????????????")
        return RetrofitClient.receivedDiaryApiService.getReceivedDiary(date)
    }

    suspend fun saveComment(saveCommentRequest: SaveCommentRequest): Response<IsSuccessResponse>{
        Log.d(TAG, "saveComment: 불린겨 뭐여?????????????")
        return RetrofitClient.receivedDiaryApiService.saveComment(saveCommentRequest)
    }

    suspend fun reportDiary(reportDiaryRequest: ReportDiaryRequest): Response<IsSuccessResponse>{
        Log.d(TAG, "reportDiary: 불린겨 뭐여?????????????")
        return RetrofitClient.receivedDiaryApiService.reportDiary(reportDiaryRequest)
    }

}