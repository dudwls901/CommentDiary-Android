package com.movingmaker.data.remote.api


import com.movingmaker.data.remote.model.request.ReportCommentRequest
import com.movingmaker.data.remote.model.response.BaseResponse
import com.movingmaker.data.remote.model.response.DiaryResponse
import com.movingmaker.data.util.ALL
import com.movingmaker.data.util.COMMENT
import com.movingmaker.data.util.DIARY
import com.movingmaker.data.util.LIKE
import com.movingmaker.data.util.MY
import com.movingmaker.data.util.REPORT
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GatherDiaryApiService {
    @GET(DIARY + MY + ALL)
    suspend fun getAllDiary(): Response<BaseResponse<MutableList<DiaryResponse>>>

    @GET(DIARY + MY)
    suspend fun getMonthDiary(@Query("date") date: String): Response<BaseResponse<MutableList<DiaryResponse>>>

    @POST(REPORT + COMMENT)
    suspend fun reportComment(@Body reportCommentRequest: ReportCommentRequest): Response<BaseResponse<String>>

    @PATCH("${COMMENT}/${LIKE}/{commentId}")
    suspend fun likeComment(@Path("commentId") commentId: Long): Response<BaseResponse<String>>

}