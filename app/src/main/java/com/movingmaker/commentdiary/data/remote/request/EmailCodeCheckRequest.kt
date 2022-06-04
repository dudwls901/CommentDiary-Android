package com.movingmaker.commentdiary.data.remote.request

import com.google.gson.annotations.SerializedName

data class EmailCodeCheckRequest(
    @SerializedName(value="email")
    val email: String,
    @SerializedName(value="code")
    val code: Int
)