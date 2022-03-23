package com.movingmaker.commentdiary.model.remote.api

import com.google.gson.annotations.SerializedName
import com.movingmaker.commentdiary.model.remote.Url
import com.movingmaker.commentdiary.model.remote.request.EditDiaryRequest
import com.movingmaker.commentdiary.model.remote.request.EmailCodeCheckRequest
import com.movingmaker.commentdiary.model.remote.request.SaveDiaryRequest
import com.movingmaker.commentdiary.model.remote.response.CommentListResponse
import com.movingmaker.commentdiary.model.remote.response.DiaryListResponse
import com.movingmaker.commentdiary.model.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.model.remote.response.SaveDiaryResponse
import retrofit2.Response
import retrofit2.http.*

interface MyDiaryApiService {
    @GET(Url.MONTH_DIARY)
    suspend fun getMonthDiary(@Query("date") date: String): Response<DiaryListResponse>

    @POST(Url.DIARY)
    suspend fun saveDiary(@Body saveDiaryRequest: SaveDiaryRequest): Response<SaveDiaryResponse>

    //path variable
    @PATCH("${Url.DIARY}/{diaryId}" )
    suspend fun editDiary(@Path("diaryId") diaryId: Long, @Body editDiaryRequest: EditDiaryRequest): Response<IsSuccessResponse>

    //path variable
    @DELETE("${Url.DIARY}/{diaryId}" )
    suspend fun deleteDiary(@Path("diaryId") diaryId: Long): Response<IsSuccessResponse>

    @GET(Url.COMMENT)
    suspend fun getMonthComment(@Query("date") date: String): Response<CommentListResponse>
}