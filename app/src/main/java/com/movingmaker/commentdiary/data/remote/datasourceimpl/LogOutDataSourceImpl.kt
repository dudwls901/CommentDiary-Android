package com.movingmaker.commentdiary.data.remote.datasourceimpl

import com.movingmaker.commentdiary.data.remote.api.LogOutApiService
import com.movingmaker.commentdiary.data.remote.datasource.LogOutDataSource
import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import retrofit2.Response
import javax.inject.Inject

class LogOutDataSourceImpl @Inject constructor(
    private val logOutApiService: LogOutApiService
): LogOutDataSource {

    override suspend fun logOut(): Response<IsSuccessResponse> {
        return logOutApiService.logOut()
    }
}