package com.movingmaker.commentdiary.model.remote.api

import com.google.gson.annotations.SerializedName
import com.movingmaker.commentdiary.model.remote.Url
import com.movingmaker.commentdiary.model.remote.request.*
import com.movingmaker.commentdiary.model.remote.response.DiaryListResponse
import com.movingmaker.commentdiary.model.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.model.remote.response.SaveDiaryResponse
import retrofit2.Response
import retrofit2.http.*

interface GatherDiaryApiService {
    @GET(Url.DIARY+Url.MY+Url.ALL)
    suspend fun getAllDiary(): Response<DiaryListResponse>

    @GET(Url.DIARY+Url.MY)
    suspend fun getMonthDiary(@Query("date") date: String): Response<DiaryListResponse>

    @POST(Url.REPORT+Url.COMMENT)
    suspend fun reportComment(@Body reportCommentRequest: ReportCommentRequest): Response<IsSuccessResponse>

    //path variable
    @PATCH("${Url.COMMENT}/${Url.LIKE}/{commentId}" )
    suspend fun likeComment(@Path("commentId") commentId: Long): Response<IsSuccessResponse>

}