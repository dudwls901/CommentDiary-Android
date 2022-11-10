package com.movingmaker.commentdiary.data.remote.api

import com.movingmaker.commentdiary.data.model.DiaryId
import com.movingmaker.commentdiary.data.remote.request.EditDiaryRequest
import com.movingmaker.commentdiary.data.remote.request.SaveDiaryRequest
import com.movingmaker.commentdiary.data.util.DIARY
import com.movingmaker.commentdiary.domain.model.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface MyDiaryApiService {

    @POST(DIARY)
    suspend fun saveDiary(@Body saveDiaryRequest: SaveDiaryRequest): Response<BaseResponse<DiaryId>>

    @PATCH("${DIARY}/{diaryId}")
    suspend fun editDiary(
        @Path("diaryId") diaryId: Long,
        @Body editDiaryRequest: EditDiaryRequest
    ): Response<BaseResponse<String>>

    @DELETE("${DIARY}/{diaryId}")
    suspend fun deleteDiary(@Path("diaryId") diaryId: Long): Response<BaseResponse<String>>

}