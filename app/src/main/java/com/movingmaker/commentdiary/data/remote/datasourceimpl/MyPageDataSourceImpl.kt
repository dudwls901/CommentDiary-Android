package com.movingmaker.commentdiary.data.remote.datasourceimpl

import com.movingmaker.commentdiary.data.remote.api.MyPageApiService
import com.movingmaker.commentdiary.data.remote.datasource.MyPageDataSource
import com.movingmaker.commentdiary.data.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.data.remote.request.KakaoSignUpRequest
import com.movingmaker.commentdiary.data.remote.response.CommentListResponse
import com.movingmaker.commentdiary.data.remote.response.CommentPushStateResponse
import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.data.remote.response.MyPageResponse
import retrofit2.Response
import javax.inject.Inject

class MyPageDataSourceImpl @Inject constructor(
    private val myPageApiService: MyPageApiService
): MyPageDataSource{

    override suspend fun signOut(): Response<IsSuccessResponse> {
        return myPageApiService.signOut()
    }

    override suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): Response<IsSuccessResponse> {
        return myPageApiService.changePassword(changePasswordRequest)
    }

    override suspend fun getMyPage(): Response<MyPageResponse> {
        return myPageApiService.getMyPage()
    }

    override suspend fun getAllComment(): Response<CommentListResponse> {
        return myPageApiService.getAllComment()
    }

    override suspend fun getMonthComment(date: String): Response<CommentListResponse> {
        return myPageApiService.getMonthComment(date)
    }

    override suspend fun patchCommentPushState(): Response<CommentPushStateResponse> {
        return myPageApiService.patchCommentPushState()
    }

    override suspend fun kakaoSignUpSetAccepts(kakaoSignUpRequest: KakaoSignUpRequest): Response<IsSuccessResponse>{
        return myPageApiService.kakaoSignUpSetAccepts(kakaoSignUpRequest)
    }
}