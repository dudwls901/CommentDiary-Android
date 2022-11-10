package com.movingmaker.commentdiary.data.repositoryimpl

import com.movingmaker.commentdiary.data.model.Comment
import com.movingmaker.commentdiary.data.model.MyInfo
import com.movingmaker.commentdiary.data.remote.datasource.MyPageDataSource
import com.movingmaker.commentdiary.data.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.domain.model.BaseResponse
import com.movingmaker.commentdiary.domain.model.NetworkResult
import com.movingmaker.commentdiary.domain.repository.MyPageRepository
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val myPageDataSource: MyPageDataSource
) : MyPageRepository {

    override suspend fun signOut(): NetworkResult<BaseResponse<String>> =
        myPageDataSource.signOut()


    override suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): NetworkResult<BaseResponse<String>> =
        myPageDataSource.changePassword(changePasswordRequest)


    override suspend fun getMyPage(): NetworkResult<BaseResponse<MyInfo>> =
        myPageDataSource.getMyPage()


    override suspend fun getAllComment(): NetworkResult<BaseResponse<List<Comment>>> =
        myPageDataSource.getAllComment()


    override suspend fun getMonthComment(date: String): NetworkResult<BaseResponse<List<Comment>>> =
        myPageDataSource.getMonthComment(date)


    override suspend fun patchCommentPushState(): NetworkResult<BaseResponse<Map<String, Char>>> =
        myPageDataSource.patchCommentPushState()

    override suspend fun logOut(): NetworkResult<BaseResponse<String>> =
        myPageDataSource.logOut()

}