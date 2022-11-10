package com.movingmaker.commentdiary.data.remote.datasourceimpl

import com.movingmaker.commentdiary.data.model.Comment
import com.movingmaker.commentdiary.data.model.MyInfo
import com.movingmaker.commentdiary.data.remote.api.MyPageApiService
import com.movingmaker.commentdiary.data.remote.datasource.MyPageDataSource
import com.movingmaker.commentdiary.data.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.data.util.safeApiCall
import com.movingmaker.commentdiary.domain.model.BaseResponse
import com.movingmaker.commentdiary.domain.model.NetworkResult
import javax.inject.Inject

class MyPageDataSourceImpl @Inject constructor(
    private val myPageApiService: MyPageApiService
) : MyPageDataSource {

    override suspend fun signOut(): NetworkResult<BaseResponse<String>> =
        safeApiCall { myPageApiService.signOut() }


    override suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): NetworkResult<BaseResponse<String>> =
        safeApiCall { myPageApiService.changePassword(changePasswordRequest) }


    override suspend fun getMyPage(): NetworkResult<BaseResponse<MyInfo>> =
        safeApiCall { myPageApiService.getMyPage() }


    override suspend fun getAllComment(): NetworkResult<BaseResponse<List<Comment>>> =
        safeApiCall { myPageApiService.getAllComment() }


    override suspend fun getMonthComment(date: String): NetworkResult<BaseResponse<List<Comment>>> =
        safeApiCall { myPageApiService.getMonthComment(date) }


    override suspend fun patchCommentPushState(): NetworkResult<BaseResponse<Map<String, Char>>> =
        safeApiCall { myPageApiService.patchCommentPushState() }

    override suspend fun logOut(): NetworkResult<BaseResponse<String>> =
        safeApiCall { myPageApiService.logOut() }

}