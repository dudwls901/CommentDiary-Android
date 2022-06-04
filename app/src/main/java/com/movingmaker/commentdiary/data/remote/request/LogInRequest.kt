package com.movingmaker.commentdiary.data.remote.request

import com.google.gson.annotations.SerializedName

data class LogInRequest(
    @SerializedName(value="email")
    val email: String,
    @SerializedName(value="password")
    val password: String,
    @SerializedName(value="deviceToken")
    val deviceToken: String
)
