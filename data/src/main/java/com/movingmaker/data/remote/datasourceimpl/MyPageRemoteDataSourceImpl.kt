package com.movingmaker.data.remote.datasourceimpl

import com.movingmaker.data.remote.api.MyPageApiService
import com.movingmaker.data.remote.datasource.MyPageRemoteDataSource
import com.movingmaker.data.util.safeApiCall
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.MyInfo
import javax.inject.Inject

class MyPageRemoteDataSourceImpl @Inject constructor(
    private val myPageApiService: MyPageApiService,
) : MyPageRemoteDataSource {

    override suspend fun signOut(): NetworkResult<BaseResponse<String>> =
        safeApiCall { myPageApiService.signOut() }

    override suspend fun getMyPage(): NetworkResult<BaseResponse<MyInfo>> =
        safeApiCall { myPageApiService.getMyPage() }

    override suspend fun patchCommentPushState(): NetworkResult<BaseResponse<Map<String, Boolean>>> =
        safeApiCall { myPageApiService.patchCommentPushState() }

    override suspend fun logOut(): NetworkResult<BaseResponse<String>> =
        safeApiCall { myPageApiService.logOut() }
}