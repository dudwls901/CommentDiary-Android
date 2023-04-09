package com.movingmaker.data.remote.api

import com.movingmaker.data.remote.model.request.ReportCommentRequest
import com.movingmaker.data.remote.model.request.ReportDiaryRequest
import com.movingmaker.data.remote.model.response.BaseResponse
import com.movingmaker.data.util.COMMENT
import com.movingmaker.data.util.DIARY
import com.movingmaker.data.util.REPORT
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ReportApiService {

    @POST(REPORT + COMMENT)
    suspend fun reportComment(@Body reportCommentRequest: ReportCommentRequest): Response<BaseResponse<String>>

    @POST(REPORT + DIARY)
    suspend fun reportDiary(@Body reportDiaryRequest: ReportDiaryRequest): Response<BaseResponse<String>>

}