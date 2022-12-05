package com.movingmaker.data.remote.api

import com.movingmaker.data.remote.model.request.EditDiaryRequest
import com.movingmaker.data.remote.model.request.SaveDiaryRequest
import com.movingmaker.data.remote.model.response.BaseResponse
import com.movingmaker.data.remote.model.response.DiaryIdResponse
import com.movingmaker.data.util.DIARY
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface MyDiaryApiService {

    @POST(DIARY)
    suspend fun saveDiary(@Body saveDiaryRequest: SaveDiaryRequest): Response<BaseResponse<DiaryIdResponse>>

    @PATCH("${DIARY}/{diaryId}")
    suspend fun editDiary(
        @Path("diaryId") diaryId: Long,
        @Body editDiaryRequest: EditDiaryRequest
    ): Response<BaseResponse<String>>

    @DELETE("${DIARY}/{diaryId}")
    suspend fun deleteDiary(@Path("diaryId") diaryId: Long): Response<BaseResponse<String>>

}