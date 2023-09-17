package com.movingmaker.data.remote.datasourceimpl

import com.movingmaker.data.remote.api.BearerApiService
import com.movingmaker.data.remote.datasource.CommentRemoteDataSource
import com.movingmaker.data.remote.model.request.SaveCommentRequest
import com.movingmaker.data.util.safeApiCall
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.Comment
import com.movingmaker.domain.model.response.SavedComment
import javax.inject.Inject

class CommentRemoteDataSourceImpl @Inject constructor(
    private val bearerApiService: BearerApiService
) : CommentRemoteDataSource {
    override suspend fun saveComment(saveCommentRequest: SaveCommentRequest): NetworkResult<BaseResponse<SavedComment>> =
        safeApiCall { bearerApiService.saveComment(saveCommentRequest) }

    override suspend fun likeComment(commentId: Long): NetworkResult<BaseResponse<String>> =
        safeApiCall { bearerApiService.likeComment(commentId) }

    override suspend fun getAllComments(): NetworkResult<BaseResponse<List<Comment>>> =
        safeApiCall { bearerApiService.getAllComments() }

    override suspend fun getPeriodComments(date: String): NetworkResult<BaseResponse<List<Comment>>> =
        safeApiCall { bearerApiService.getPeriodComments(date) }
}