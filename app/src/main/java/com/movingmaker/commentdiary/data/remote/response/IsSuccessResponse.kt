package com.movingmaker.commentdiary.data.remote.response

import com.google.gson.annotations.SerializedName

data class IsSuccessResponse(
    @SerializedName(value = "code")
    val code: Int,
    @SerializedName(value = "message")
    val message: String
)
