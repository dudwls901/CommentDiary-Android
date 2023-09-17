package com.movingmaker.data.remote.api

import com.movingmaker.data.remote.model.response.AuthTokensResponse
import com.movingmaker.data.remote.model.response.BaseResponse
import com.movingmaker.data.util.REISSUE
import retrofit2.Response
import retrofit2.http.POST

interface XAuthTokenAndRefreshTokenApiService{

    /*
    * MEMBER
    * */

    @POST(REISSUE)
    suspend fun reIssueToken(): Response<BaseResponse<AuthTokensResponse>>

}