package com.movingmaker.data.repositoryimpl

import com.movingmaker.data.remote.datasource.CommentRemoteDataSource
import com.movingmaker.data.remote.model.request.toDataModel
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.request.SaveCommentModel
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.Comment
import com.movingmaker.domain.repository.CommentRepository
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val commentRemoteDataSource: CommentRemoteDataSource
) : CommentRepository {
    override suspend fun saveComment(saveCommentModel: SaveCommentModel): NetworkResult<BaseResponse<String>> =
        commentRemoteDataSource.saveComment(saveCommentModel.toDataModel())

    /**
     * 코멘트 좋아요 누르기
     * */
    override suspend fun likeComment(commentId: Long): NetworkResult<BaseResponse<String>> =
        commentRemoteDataSource.likeComment(commentId)

    override suspend fun getAllComments(): NetworkResult<BaseResponse<List<Comment>>> =
        commentRemoteDataSource.getAllComments()

    override suspend fun getPeriodComments(date: String): NetworkResult<BaseResponse<List<Comment>>> =
        commentRemoteDataSource.getPeriodComments(date)
}