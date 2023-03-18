package com.movingmaker.data.repositoryimpl

import com.movingmaker.data.remote.datasource.MyPageRemoteDataSource
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.MyInfo
import com.movingmaker.domain.repository.MyPageRepository
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val myPageRemoteDataSource: MyPageRemoteDataSource
) : MyPageRepository {

    override suspend fun patchCommentPushState(): NetworkResult<BaseResponse<Map<String, Char>>> =
        myPageRemoteDataSource.patchCommentPushState()

    override suspend fun signOut(): NetworkResult<BaseResponse<String>> =
        myPageRemoteDataSource.signOut()


    override suspend fun getMyPage(): NetworkResult<BaseResponse<MyInfo>> =
        myPageRemoteDataSource.getMyPage()

    override suspend fun logOut(): NetworkResult<BaseResponse<String>> =
        myPageRemoteDataSource.logOut()

}