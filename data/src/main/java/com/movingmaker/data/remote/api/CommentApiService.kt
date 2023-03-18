package com.movingmaker.data.remote.api

import com.movingmaker.data.remote.model.request.SaveCommentRequest
import com.movingmaker.data.remote.model.response.BaseResponse
import com.movingmaker.data.remote.model.response.CommentResponse
import com.movingmaker.data.util.ALL
import com.movingmaker.data.util.COMMENT
import com.movingmaker.data.util.LIKE
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentApiService {

    @PATCH("$COMMENT/$LIKE/{commentId}")
    suspend fun likeComment(@Path("commentId") commentId: Long): Response<BaseResponse<String>>

    @POST(COMMENT)
    suspend fun saveComment(@Body saveCommentRequest: SaveCommentRequest): Response<BaseResponse<String>>

    @GET(COMMENT + ALL)
    suspend fun getAllComments(): Response<BaseResponse<List<CommentResponse>>>

    //ymd format이면 하루 코멘트, ym format이면 한 달 코멘트
    @GET(COMMENT)
    suspend fun getPeriodComments(@Query("date") date: String): Response<BaseResponse<List<CommentResponse>>>

}