package com.movingmaker.commentdiary.model.remote.api

import com.movingmaker.commentdiary.model.remote.Url
import com.movingmaker.commentdiary.model.remote.response.DiaryListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MyDiaryApiService {
    @GET(Url.MONTH_DIARY)
    suspend fun getMonthDiary(@Query("date") date: String): Response<DiaryListResponse>
}