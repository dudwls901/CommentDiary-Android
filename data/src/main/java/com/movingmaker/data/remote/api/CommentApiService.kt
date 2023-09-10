package com.movingmaker.data.remote.api

import com.movingmaker.data.remote.model.request.SaveCommentRequest
import com.movingmaker.data.remote.model.response.BaseResponse
import com.movingmaker.data.remote.model.response.CommentResponse
import com.movingmaker.data.remote.model.response.SavedCommentResponse
import com.movingmaker.data.util.COMMENTS
import com.movingmaker.data.util.COMMENTS_ALL
import com.movingmaker.data.util.COMMENTS_LIKE
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentApiService {

    @PATCH("${COMMENTS_LIKE}/{commentId}")
    suspend fun likeComment(@Path("commentId") commentId: Long): Response<BaseResponse<String>>

    @POST(COMMENTS)
    suspend fun saveComment(@Body saveCommentRequest: SaveCommentRequest): Response<BaseResponse<SavedCommentResponse>>

    @GET(COMMENTS_ALL)
    suspend fun getAllComments(): Response<BaseResponse<List<CommentResponse>>>

    //ymd format이면 하루 코멘트, ym format이면 한 달 코멘트
    @GET(COMMENTS)
    suspend fun getPeriodComments(@Query("date") date: String): Response<BaseResponse<List<CommentResponse>>>

}