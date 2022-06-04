package com.movingmaker.commentdiary.data.repository

import com.movingmaker.commentdiary.data.remote.RetrofitClient
import com.movingmaker.commentdiary.data.remote.request.ReportCommentRequest
import com.movingmaker.commentdiary.data.remote.response.DiaryListResponse
import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import retrofit2.Response

class GatherDiaryRepository {

    companion object{
        val INSTANCE = GatherDiaryRepository()
        val TAG = "레트로핏 로그"
    }

    suspend fun getMonthDiary(date: String): Response<DiaryListResponse> {
        return RetrofitClient.gatherDiaryApiService.getMonthDiary(date)
    }

    suspend fun getAllDiary(): Response<DiaryListResponse>{
        return RetrofitClient.gatherDiaryApiService.getAllDiary()
    }

    suspend fun reportComment(reportCommentRequest: ReportCommentRequest): Response<IsSuccessResponse>{
        return RetrofitClient.gatherDiaryApiService.reportComment(reportCommentRequest)
    }

    suspend fun likeComment(commentId: Long): Response<IsSuccessResponse>{
        return RetrofitClient.gatherDiaryApiService.likeComment(commentId)
    }

}