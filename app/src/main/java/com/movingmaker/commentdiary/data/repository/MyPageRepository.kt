package com.movingmaker.commentdiary.data.repository

import com.movingmaker.commentdiary.data.remote.RetrofitClient
import com.movingmaker.commentdiary.data.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.data.remote.response.CommentListResponse
import com.movingmaker.commentdiary.data.remote.response.CommentPushStateResponse
import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.data.remote.response.MyPageResponse
import retrofit2.Response

class MyPageRepository {

    companion object{
        val INSTANCE = MyPageRepository()
        val TAG = "레트로핏 로그"
    }

    suspend fun signOut(): Response<IsSuccessResponse> {
        return RetrofitClient.myPageApiService.signOut()
    }

    suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): Response<IsSuccessResponse>{
        return RetrofitClient.myPageApiService.changePassword(changePasswordRequest)
    }

    suspend fun getMyPage(): Response<MyPageResponse>{
        return RetrofitClient.myPageApiService.getMyPage()
    }
    suspend fun getAllComment(): Response<CommentListResponse>{
        return RetrofitClient.myPageApiService.getAllComment()
    }

    suspend fun getMonthComment(date: String): Response<CommentListResponse>{
        return RetrofitClient.myPageApiService.getMonthComment(date)
    }

    suspend fun patchCommentPushState(): Response<CommentPushStateResponse>{
        return RetrofitClient.myPageApiService.patchCommentPushState()
    }
}