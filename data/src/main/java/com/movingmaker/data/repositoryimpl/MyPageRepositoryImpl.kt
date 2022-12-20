package com.movingmaker.data.repositoryimpl

import com.movingmaker.data.remote.datasource.MyPageDataSource
import com.movingmaker.data.remote.model.request.toDataModel
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.request.ChangePasswordModel
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.Comment
import com.movingmaker.domain.model.response.MyInfo
import com.movingmaker.domain.repository.MyPageRepository
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val myPageDataSource: MyPageDataSource
) : MyPageRepository {

    override suspend fun signOut(): NetworkResult<BaseResponse<String>> =
        myPageDataSource.signOut()


    override suspend fun changePassword(changePasswordModel: ChangePasswordModel): NetworkResult<BaseResponse<String>> =
        myPageDataSource.changePassword(changePasswordModel.toDataModel())


    override suspend fun getMyPage(): NetworkResult<BaseResponse<MyInfo>> =
        myPageDataSource.getMyPage()


    override suspend fun getAllComment(): NetworkResult<BaseResponse<List<Comment>>> =
        myPageDataSource.getAllComment()


    override suspend fun getPeriodComment(date: String): NetworkResult<BaseResponse<List<Comment>>> =
        myPageDataSource.getPeriodComment(date)


    override suspend fun patchCommentPushState(): NetworkResult<BaseResponse<Map<String, Char>>> =
        myPageDataSource.patchCommentPushState()

    override suspend fun logOut(): NetworkResult<BaseResponse<String>> =
        myPageDataSource.logOut()

}