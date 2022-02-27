package com.movingmaker.commentdiary.model.remote.response

import com.google.gson.annotations.SerializedName

data class EmailCodeResponse(
    val message: String,
    val code: Int
)
