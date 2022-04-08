package com.movingmaker.commentdiary.model.remote.response

import com.google.gson.annotations.SerializedName
import com.movingmaker.commentdiary.model.entity.AuthTokens
import com.movingmaker.commentdiary.model.entity.MyInfo

data class MyPageResponse(
    @SerializedName(value = "code")
    val code: Int,
    @SerializedName(value = "message")
    val message: String,
    @SerializedName(value = "result")
    val result: MyInfo
)