package com.movingmaker.data.remote.api


import com.movingmaker.data.remote.model.response.BaseResponse
import com.movingmaker.data.remote.model.response.ReceivedDiaryResponse
import com.movingmaker.data.util.DELIVERY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ReceivedDiaryApiService {
    @GET(DELIVERY)
    suspend fun getReceivedDiary(@Query("date") date: String): Response<BaseResponse<ReceivedDiaryResponse>>
}