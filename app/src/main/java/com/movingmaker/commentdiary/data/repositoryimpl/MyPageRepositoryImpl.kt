package com.movingmaker.commentdiary.data.repositoryimpl

import com.movingmaker.commentdiary.data.remote.datasource.MyPageDataSource
import com.movingmaker.commentdiary.data.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.data.remote.request.KakaoSignUpRequest
import com.movingmaker.commentdiary.data.remote.response.CommentListResponse
import com.movingmaker.commentdiary.data.remote.response.CommentPushStateResponse
import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.data.remote.response.MyPageResponse
import com.movingmaker.commentdiary.domain.repository.MyPageRepository
import retrofit2.Response
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val myPageDataSource: MyPageDataSource
): MyPageRepository {

    override suspend fun signOut(): Response<IsSuccessResponse> {
        return myPageDataSource.signOut()
    }

    override suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): Response<IsSuccessResponse> {
        return myPageDataSource.changePassword(changePasswordRequest)
    }

    override suspend fun getMyPage(): Response<MyPageResponse> {
        return myPageDataSource.getMyPage()
    }

    override suspend fun getAllComment(): Response<CommentListResponse> {
        return myPageDataSource.getAllComment()
    }

    override suspend fun getMonthComment(date: String): Response<CommentListResponse> {
        return myPageDataSource.getMonthComment(date)
    }

    override suspend fun patchCommentPushState(): Response<CommentPushStateResponse> {
        return myPageDataSource.patchCommentPushState()
    }

    override suspend fun kakaoSignUpSetAccepts(kakaoSignUpRequest: KakaoSignUpRequest): Response<IsSuccessResponse> {
        return myPageDataSource.kakaoSignUpSetAccepts(kakaoSignUpRequest)
    }
}