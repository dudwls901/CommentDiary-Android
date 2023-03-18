package com.movingmaker.data.remote.datasource

import com.movingmaker.data.remote.model.request.SaveCommentRequest
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.Comment

interface CommentRemoteDataSource {

    suspend fun saveComment(saveCommentRequest: SaveCommentRequest): NetworkResult<BaseResponse<String>>

    suspend fun likeComment(commentId: Long): NetworkResult<BaseResponse<String>>

    suspend fun getAllComments(): NetworkResult<BaseResponse<List<Comment>>>

    suspend fun getPeriodComments(date: String): NetworkResult<BaseResponse<List<Comment>>>
}