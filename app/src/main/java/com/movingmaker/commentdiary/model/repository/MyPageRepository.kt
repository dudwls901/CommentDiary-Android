package com.movingmaker.commentdiary.model.repository

import android.util.Log
import com.movingmaker.commentdiary.model.remote.RetrofitClient
import com.movingmaker.commentdiary.model.remote.request.ChangePasswordRequest
import com.movingmaker.commentdiary.model.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.model.remote.response.MyPageResponse
import retrofit2.Response

class MyPageRepository {

    companion object{
        val INSTANCE = MyPageRepository()
        val TAG = "레트로핏 로그"
    }

    suspend fun signOut(): Response<IsSuccessResponse> {
        Log.d(TAG, "signOut: 불린겨 뭐여?????????????")
        return RetrofitClient.myPageApiService.signOut()
    }

    suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): Response<IsSuccessResponse>{
        Log.d(TAG, "changePassword: ")
        return RetrofitClient.myPageApiService.changePassword(changePasswordRequest)
    }

    suspend fun getMyPage(): Response<MyPageResponse>{
        Log.d(TAG, "getMyPage: 불린겨 뭐여?????????????")
        return RetrofitClient.myPageApiService.getMyPage()
    }
}