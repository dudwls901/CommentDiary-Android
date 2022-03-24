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
        return RetrofitClient.receivedDiaryApiService.getReceivedDiary(date)
    }

    suspend fun saveComment(saveCommentRequest: SaveCommentRequest): Response<IsSuccessResponse>{
        return RetrofitClient.receivedDiaryApiService.saveComment(saveCommentRequest)
    }

    suspend fun reportDiary(reportDiaryRequest: ReportDiaryRequest): Response<IsSuccessResponse>{
        return RetrofitClient.receivedDiaryApiService.reportDiary(reportDiaryRequest)
    }

}