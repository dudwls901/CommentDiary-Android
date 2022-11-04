package com.movingmaker.commentdiary.data.remote.api

import com.movingmaker.commentdiary.common.util.Url.COMMENT
import com.movingmaker.commentdiary.common.util.Url.DELIVERY
import com.movingmaker.commentdiary.common.util.Url.DIARY
import com.movingmaker.commentdiary.common.util.Url.REPORT
import com.movingmaker.commentdiary.data.model.ReceivedDiary
import com.movingmaker.commentdiary.data.remote.request.ReportDiaryRequest
import com.movingmaker.commentdiary.data.remote.request.SaveCommentRequest
import com.movingmaker.commentdiary.domain.model.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ReceivedDiaryApiService {
    @GET(DELIVERY)
    suspend fun getReceivedDiary(@Query("date") date: String): Response<BaseResponse<ReceivedDiary>>

    @POST(COMMENT)
    suspend fun saveComment(@Body saveCommentRequest: SaveCommentRequest): Response<BaseResponse<String>>

    @POST(REPORT + DIARY)
    suspend fun reportDiary(@Body reportDiaryRequest: ReportDiaryRequest): Response<BaseResponse<String>>
}