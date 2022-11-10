package com.movingmaker.commentdiary.data.remote.api

import com.movingmaker.commentdiary.data.model.Diary
import com.movingmaker.commentdiary.data.remote.request.ReportCommentRequest
import com.movingmaker.commentdiary.data.util.ALL
import com.movingmaker.commentdiary.data.util.COMMENT
import com.movingmaker.commentdiary.data.util.DIARY
import com.movingmaker.commentdiary.data.util.LIKE
import com.movingmaker.commentdiary.data.util.MY
import com.movingmaker.commentdiary.data.util.REPORT
import com.movingmaker.commentdiary.domain.model.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GatherDiaryApiService {
    @GET(DIARY + MY + ALL)
    suspend fun getAllDiary(): Response<BaseResponse<MutableList<Diary>>>

    @GET(DIARY + MY)
    suspend fun getMonthDiary(@Query("date") date: String): Response<BaseResponse<MutableList<Diary>>>

    @POST(REPORT + COMMENT)
    suspend fun reportComment(@Body reportCommentRequest: ReportCommentRequest): Response<BaseResponse<String>>

    @PATCH("${COMMENT}/${LIKE}/{commentId}")
    suspend fun likeComment(@Path("commentId") commentId: Long): Response<BaseResponse<String>>

}