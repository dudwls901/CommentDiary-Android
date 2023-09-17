package com.movingmaker.data.remote.model.response

import com.movingmaker.data.remote.model.RemoteResponse
import com.movingmaker.domain.model.response.Login
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("tokenResponse")
    val authTokensResponse: AuthTokensResponse,
    val userId: Long,
) : RemoteResponse {
    override fun toDomainModel() = Login(
        authTokens = authTokensResponse.toDomainModel(),
        userId = userId
    )
}