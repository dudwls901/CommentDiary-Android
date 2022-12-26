package com.movingmaker.data.remote.model.response

import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.response.AuthTokens
import kotlinx.serialization.Serializable

@Serializable
data class AuthTokensResponse(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresIn: Long,
    val newMember: Boolean? = null,
    val userId: Long
) : RemoteModel {
    override fun toDomainModel() = AuthTokens(
        grantType, accessToken, refreshToken, accessTokenExpiresIn, newMember, userId
    )
}

fun AuthTokens.toDataModel() = AuthTokensResponse(
    grantType, accessToken, refreshToken, accessTokenExpiresIn, isNewMember, userId
)