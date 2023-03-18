package com.movingmaker.data.remote.datasourceimpl

import com.movingmaker.data.remote.api.CommentApiService
import com.movingmaker.data.remote.datasource.CommentRemoteDataSource
import com.movingmaker.data.remote.model.request.SaveCommentRequest
import com.movingmaker.data.util.safeApiCall
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.Comment
import javax.inject.Inject

class CommentRemoteDataSourceImpl @Inject constructor(
    private val commentApiService: CommentApiService
) : CommentRemoteDataSource {
    override suspend fun saveComment(saveCommentRequest: SaveCommentRequest): NetworkResult<BaseResponse<String>> =
        safeApiCall { commentApiService.saveComment(saveCommentRequest) }

    override suspend fun likeComment(commentId: Long): NetworkResult<BaseResponse<String>> =
        safeApiCall { commentApiService.likeComment(commentId) }

    override suspend fun getAllComments(): NetworkResult<BaseResponse<List<Comment>>> =
        safeApiCall { commentApiService.getAllComments() }

    override suspend fun getPeriodComments(date: String): NetworkResult<BaseResponse<List<Comment>>> =
        safeApiCall { commentApiService.getPeriodComments(date) }
}