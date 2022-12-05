package com.movingmaker.data.remote.model.response

import com.google.gson.annotations.SerializedName
import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.response.AuthTokens

data class AuthTokensResponse(
    @SerializedName("grantType")
    val grantType: String,
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("accessTokenExpiresIn")
    val accessTokenExpiresIn: Long,
    @SerializedName("newMember")
    val isNewMember: Boolean?
) : RemoteModel {
    override fun toDomainModel() = AuthTokens(
        grantType, accessToken, refreshToken, accessTokenExpiresIn, isNewMember
    )
}

fun AuthTokens.toDataModel() = AuthTokensResponse(
    grantType, accessToken, refreshToken, accessTokenExpiresIn, isNewMember
)