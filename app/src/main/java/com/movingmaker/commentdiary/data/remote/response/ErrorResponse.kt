package com.movingmaker.commentdiary.data.remote.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse (
    @SerializedName(value = "status")
    val status: Int,
    @SerializedName(value = "code")
    val code: String,
    @SerializedName(value = "message")
    val message: String
)