package com.movingmaker.commentdiary.data.entity

import com.google.gson.annotations.SerializedName

data class AuthTokens(
    @SerializedName(value="grantType")
    val grantType: String,
    @SerializedName(value="accessToken")
    val accessToken: String,
    @SerializedName(value="refreshToken")
    val refreshToken: String,
    @SerializedName(value="accessTokenExpiresIn")
    val accessTokenExpiresIn: Long
)
