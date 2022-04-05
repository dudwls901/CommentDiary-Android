package com.movingmaker.commentdiary.model.remote.request

import com.google.gson.annotations.SerializedName

data class LogInRequest(
    val email: String,
    val password: String,
    @SerializedName(value="deviceToken")
    val deviceToken: String
)
