package com.movingmaker.commentdiary.model.remote.api

import com.google.gson.annotations.SerializedName
import com.movingmaker.commentdiary.model.remote.request.*
import com.movingmaker.commentdiary.model.remote.response.DiaryListResponse
import com.movingmaker.commentdiary.model.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.util.Url.ALL
import com.movingmaker.commentdiary.util.Url.COMMENT
import com.movingmaker.commentdiary.util.Url.DIARY
import com.movingmaker.commentdiary.util.Url.LIKE
import com.movingmaker.commentdiary.util.Url.MY
import com.movingmaker.commentdiary.util.Url.REPORT
import retrofit2.Response
import retrofit2.http.*

interface GatherDiaryApiService {
    @GET(DIARY+MY+ALL)
    suspend fun getAllDiary(): Response<DiaryListResponse>

    @GET(DIARY+MY)
    suspend fun getMonthDiary(@Query("date") date: String): Response<DiaryListResponse>

    @POST(REPORT+COMMENT)
    suspend fun reportComment(@Body reportCommentRequest: ReportCommentRequest): Response<IsSuccessResponse>

    //path variable
    @PATCH("${COMMENT}/${LIKE}/{commentId}" )
    suspend fun likeComment(@Path("commentId") commentId: Long): Response<IsSuccessResponse>

}