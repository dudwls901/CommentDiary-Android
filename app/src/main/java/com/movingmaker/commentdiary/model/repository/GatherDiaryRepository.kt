package com.movingmaker.commentdiary.model.repository

import android.util.Log
import com.movingmaker.commentdiary.model.remote.RetrofitClient
import com.movingmaker.commentdiary.model.remote.request.EditDiaryRequest
import com.movingmaker.commentdiary.model.remote.request.ReportCommentRequest
import com.movingmaker.commentdiary.model.remote.request.ReportDiaryRequest
import com.movingmaker.commentdiary.model.remote.request.SaveDiaryRequest
import com.movingmaker.commentdiary.model.remote.response.DiaryListResponse
import com.movingmaker.commentdiary.model.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.model.remote.response.SaveDiaryResponse
import retrofit2.Response

class GatherDiaryRepository {

    companion object{
        val INSTANCE = GatherDiaryRepository()
        val TAG = "레트로핏 로그"
    }

    suspend fun getMonthDiary(date: String): Response<DiaryListResponse> {
        Log.d(TAG, "getMonthDiary: 불린겨 뭐여?????????????")
        return RetrofitClient.gatherDiaryApiService.getMonthDiary(date)
    }

    suspend fun getAllDiary(): Response<DiaryListResponse>{
        Log.d(TAG, "getAllDiary: 불린겨 뭐여?????????????")
        return RetrofitClient.gatherDiaryApiService.getAllDiary()
    }

    suspend fun reportComment(reportCommentRequest: ReportCommentRequest): Response<IsSuccessResponse>{
        Log.d(ReceivedDiaryRepository.TAG, "reportComment: 불린겨 뭐여?????????????")
        return RetrofitClient.gatherDiaryApiService.reportComment(reportCommentRequest)
    }

    suspend fun likeComment(commentId: Long): Response<IsSuccessResponse>{
        Log.d(ReceivedDiaryRepository.TAG, "likeComment: 불린겨 뭐여?????????????")
        return RetrofitClient.gatherDiaryApiService.likeComment(commentId)
    }

}