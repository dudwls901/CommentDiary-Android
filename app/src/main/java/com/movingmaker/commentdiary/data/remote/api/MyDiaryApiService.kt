package com.movingmaker.commentdiary.data.remote.api

import com.movingmaker.commentdiary.data.remote.request.EditDiaryRequest
import com.movingmaker.commentdiary.data.remote.request.SaveDiaryRequest
import com.movingmaker.commentdiary.data.remote.response.DiaryListResponse
import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.data.remote.response.SaveDiaryResponse
import com.movingmaker.commentdiary.common.util.Url.DIARY
import com.movingmaker.commentdiary.common.util.Url.MONTH_DIARY
import retrofit2.Response
import retrofit2.http.*

interface MyDiaryApiService {
    @GET(MONTH_DIARY)
    suspend fun getMonthDiary(@Query("date") date: String): Response<DiaryListResponse>

    @POST(DIARY)
    suspend fun saveDiary(@Body saveDiaryRequest: SaveDiaryRequest): Response<SaveDiaryResponse>

    //path variable
    @PATCH("${DIARY}/{diaryId}")
    suspend fun editDiary(
        @Path("diaryId") diaryId: Long,
        @Body editDiaryRequest: EditDiaryRequest
    ): Response<IsSuccessResponse>

    //path variable
    @DELETE("${DIARY}/{diaryId}")
    suspend fun deleteDiary(@Path("diaryId") diaryId: Long): Response<IsSuccessResponse>

}