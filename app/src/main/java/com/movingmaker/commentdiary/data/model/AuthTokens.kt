package com.movingmaker.commentdiary.data.model

import com.google.gson.annotations.SerializedName

data class AuthTokens(
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
)
