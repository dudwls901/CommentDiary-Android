package com.movingmaker.data.remote.api

import com.movingmaker.data.remote.model.request.EditDiaryRequest
import com.movingmaker.data.remote.model.request.SaveDiaryRequest
import com.movingmaker.data.remote.model.response.BaseResponse
import com.movingmaker.data.remote.model.response.DiaryIdResponse
import com.movingmaker.data.remote.model.response.DiaryResponse
import com.movingmaker.data.util.DIARY
import com.movingmaker.data.util.DIARY_ALL
import com.movingmaker.data.util.DIARY_MY
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface DiaryApiService {

    @POST(DIARY)
    suspend fun saveDiary(@Body saveDiaryRequest: SaveDiaryRequest): Response<BaseResponse<DiaryIdResponse>>

    @PATCH("${DIARY}/{diaryId}")
    suspend fun editDiary(
        @Path("diaryId") diaryId: Long,
        @Body editDiaryRequest: EditDiaryRequest
    ): Response<BaseResponse<String>>

    @DELETE("${DIARY}/{diaryId}")
    suspend fun deleteDiary(@Path("diaryId") diaryId: Long): Response<BaseResponse<String>>

    @GET(DIARY_ALL)
    suspend fun getAllDiaries(): Response<BaseResponse<List<DiaryResponse>>>

    /**
     * date = ym인 경우 한 달 일기, date = ymd인 경우 하루 일기
     * */
    @GET(DIARY_MY)
    suspend fun getPeriodDiaries(@Query("date") date: String): Response<BaseResponse<List<DiaryResponse>>>

    @GET("${DIARY}/{diaryId}")
    suspend fun getDiary(@Path("diaryId") diaryId: Long): Response<BaseResponse<DiaryResponse>>

}