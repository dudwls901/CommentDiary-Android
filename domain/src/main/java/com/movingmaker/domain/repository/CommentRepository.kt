package com.movingmaker.domain.repository

import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.request.SaveCommentModel
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.Comment

interface CommentRepository {

    suspend fun saveComment(saveCommentModel: SaveCommentModel): NetworkResult<BaseResponse<String>>

    suspend fun likeComment(commentId: Long): NetworkResult<BaseResponse<String>>

    suspend fun getAllComments(): NetworkResult<BaseResponse<List<Comment>>>

    suspend fun getPeriodComments(date: String): NetworkResult<BaseResponse<List<Comment>>>

}