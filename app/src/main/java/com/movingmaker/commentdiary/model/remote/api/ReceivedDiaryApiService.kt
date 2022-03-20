package com.movingmaker.commentdiary.model.remote.api

import com.google.gson.annotations.SerializedName
import com.movingmaker.commentdiary.model.entity.ReceivedDiary
import com.movingmaker.commentdiary.model.remote.Url
import com.movingmaker.commentdiary.model.remote.request.*
import com.movingmaker.commentdiary.model.remote.response.*
import retrofit2.Response
import retrofit2.http.*

interface ReceivedDiaryApiService {
    @GET(Url.DELIVERY)
    suspend fun getReceivedDiary(@Query("date") date: String): Response<ReceivedDiaryResponse>

    @POST(Url.COMMENT)
    suspend fun saveComment(@Body saveCommentRequest: SaveCommentRequest): Response<IsSuccessResponse>

    @POST(Url.REPORT+Url.DIARY)
    suspend fun reportDiary(@Body reportDiaryRequest: ReportDiaryRequest): Response<IsSuccessResponse>
}