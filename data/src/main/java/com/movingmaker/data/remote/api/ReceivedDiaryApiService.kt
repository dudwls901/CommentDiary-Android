package com.movingmaker.data.remote.api


import com.movingmaker.data.remote.model.request.ReportDiaryRequest
import com.movingmaker.data.remote.model.request.SaveCommentRequest
import com.movingmaker.data.remote.model.response.BaseResponse
import com.movingmaker.data.remote.model.response.ReceivedDiaryResponse
import com.movingmaker.data.util.COMMENT
import com.movingmaker.data.util.DELIVERY
import com.movingmaker.data.util.DIARY
import com.movingmaker.data.util.REPORT
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ReceivedDiaryApiService {
    @GET(DELIVERY)
    suspend fun getReceivedDiary(@Query("date") date: String): Response<BaseResponse<ReceivedDiaryResponse>>

    @POST(COMMENT)
    suspend fun saveComment(@Body saveCommentRequest: SaveCommentRequest): Response<BaseResponse<String>>

    @POST(REPORT + DIARY)
    suspend fun reportDiary(@Body reportDiaryRequest: ReportDiaryRequest): Response<BaseResponse<String>>
}