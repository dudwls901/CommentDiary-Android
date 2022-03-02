package com.movingmaker.commentdiary.model.remote.api

import com.movingmaker.commentdiary.model.remote.Url
import com.movingmaker.commentdiary.model.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.model.remote.response.DiaryListResponse
import com.movingmaker.commentdiary.model.remote.response.IsSuccessResponse
import retrofit2.Response
import retrofit2.http.*

interface MyDiaryApiService {
    @GET(Url.MONTH_DIARY)
    suspend fun getMonthDiary(@Query("date") date: String): Response<DiaryListResponse>
}