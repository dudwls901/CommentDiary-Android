package com.movingmaker.commentdiary.model.remote.api

import com.google.gson.annotations.SerializedName
import com.movingmaker.commentdiary.model.remote.Url
import com.movingmaker.commentdiary.model.remote.request.EditDiaryRequest
import com.movingmaker.commentdiary.model.remote.request.EmailCodeCheckRequest
import com.movingmaker.commentdiary.model.remote.request.SaveDiaryRequest
import com.movingmaker.commentdiary.model.remote.response.DiaryListResponse
import com.movingmaker.commentdiary.model.remote.response.DiaryResponse
import com.movingmaker.commentdiary.model.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.model.remote.response.SaveDiaryResponse
import retrofit2.Response
import retrofit2.http.*

interface ReceivedDiaryApiService {
    @GET(Url.DELIVERY)
    suspend fun getReceivedDiary(@Query("date") date: String): Response<DiaryResponse>
}