package com.movingmaker.commentdiary.model.remote.response

import com.google.gson.annotations.SerializedName

data class IsSuccessResponse(
    @SerializedName(value = "code")
    val code: Int,
    @SerializedName(value = "message")
    val message: String
)
