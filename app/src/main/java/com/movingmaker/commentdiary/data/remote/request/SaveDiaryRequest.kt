package com.movingmaker.commentdiary.data.remote.request

import com.google.gson.annotations.SerializedName

data class SaveDiaryRequest(
    @SerializedName(value="title")
    val title: String,
    @SerializedName(value="content")
    val content: String,
    @SerializedName(value="date")
    val date: String,
    @SerializedName(value="deliveryYn")
    val deliveryYN: Char,
    @SerializedName(value="tempYn")
    val tempYN: Char,
)