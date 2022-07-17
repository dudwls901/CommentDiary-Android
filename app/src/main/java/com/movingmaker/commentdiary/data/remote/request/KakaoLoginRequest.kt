package com.movingmaker.commentdiary.data.remote.request

import com.google.gson.annotations.SerializedName

data class KakaoLoginRequest(
    @SerializedName(value="loginType")
    val loginType: String,
    @SerializedName(value="accessToken")
    val accessToken: String,
    @SerializedName(value="deviceToken")
    val deviceToken: String
)