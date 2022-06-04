package com.movingmaker.commentdiary.data.remote.request

import com.google.gson.annotations.SerializedName

data class EditDiaryRequest(
    @SerializedName(value="title")
    val title: String,
    @SerializedName(value="content")
    val content: String,
    @SerializedName(value="tempYn")
    val tempYN: Char,
)