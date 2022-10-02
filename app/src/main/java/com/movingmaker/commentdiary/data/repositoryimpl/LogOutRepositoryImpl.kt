package com.movingmaker.commentdiary.data.repositoryimpl

import com.movingmaker.commentdiary.data.remote.datasource.LogOutDataSource
import com.movingmaker.commentdiary.data.remote.response.IsSuccessResponse
import com.movingmaker.commentdiary.domain.repository.LogOutRepository
import retrofit2.Response
import javax.inject.Inject

class LogOutRepositoryImpl @Inject constructor(
    private val logOutDataSource: LogOutDataSource
) : LogOutRepository {
    override suspend fun logOut(): Response<IsSuccessResponse> {
        return logOutDataSource.logOut()
    }
}