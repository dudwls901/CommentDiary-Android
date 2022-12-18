package com.movingmaker.data.remote.datasourceimpl

import com.movingmaker.data.remote.api.LogOutApiService
import com.movingmaker.data.remote.api.MyPageApiService
import com.movingmaker.data.remote.datasource.MyPageDataSource
import com.movingmaker.data.remote.model.request.ChangePasswordRequest
import com.movingmaker.data.util.safeApiCall
import com.movingmaker.domain.model.NetworkResult
import com.movingmaker.domain.model.response.BaseResponse
import com.movingmaker.domain.model.response.Comment
import com.movingmaker.domain.model.response.MyInfo
import javax.inject.Inject

class MyPageDataSourceImpl @Inject constructor(
    private val myPageApiService: MyPageApiService,
    private val logOutApiService: LogOutApiService
) : MyPageDataSource {

    override suspend fun signOut(): NetworkResult<BaseResponse<String>> =
        safeApiCall { myPageApiService.signOut() }

    override suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): NetworkResult<BaseResponse<String>> =
        safeApiCall { myPageApiService.changePassword(changePasswordRequest) }

    override suspend fun getMyPage(): NetworkResult<BaseResponse<MyInfo>> =
        safeApiCall { myPageApiService.getMyPage() }

    override suspend fun getAllComment(): NetworkResult<BaseResponse<List<Comment>>> =
        safeApiCall { myPageApiService.getAllComment() }

    override suspend fun getPeriodComment(date: String): NetworkResult<BaseResponse<List<Comment>>> =
        safeApiCall { myPageApiService.getPeriodComment(date) }

    override suspend fun patchCommentPushState(): NetworkResult<BaseResponse<Map<String, Char>>> =
        safeApiCall { myPageApiService.patchCommentPushState() }

    override suspend fun logOut(): NetworkResult<BaseResponse<String>> =
        safeApiCall { logOutApiService.logOut() }
}