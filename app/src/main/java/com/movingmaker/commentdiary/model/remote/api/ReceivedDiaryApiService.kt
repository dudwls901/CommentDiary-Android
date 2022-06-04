package com.movingmaker.commentdiary.model.remote.api

import com.google.gson.annotations.SerializedName
import com.movingmaker.commentdiary.model.entity.ReceivedDiary
import com.movingmaker.commentdiary.model.remote.request.*
import com.movingmaker.commentdiary.model.remote.response.*
import com.movingmaker.commentdiary.util.Url.COMMENT
import com.movingmaker.commentdiary.util.Url.DELIVERY
import com.movingmaker.commentdiary.util.Url.DIARY
import com.movingmaker.commentdiary.util.Url.REPORT
import retrofit2.Response
import retrofit2.http.*

interface ReceivedDiaryApiService {
    @GET(DELIVERY)
    suspend fun getReceivedDiary(@Query("date") date: String): Response<ReceivedDiaryResponse>

    @POST(COMMENT)
    suspend fun saveComment(@Body saveCommentRequest: SaveCommentRequest): Response<IsSuccessResponse>

    @POST(REPORT+DIARY)
    suspend fun reportDiary(@Body reportDiaryRequest: ReportDiaryRequest): Response<IsSuccessResponse>
}