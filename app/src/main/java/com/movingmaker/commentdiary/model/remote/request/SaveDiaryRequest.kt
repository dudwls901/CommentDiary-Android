package com.movingmaker.commentdiary.model.remote.request

import com.google.gson.annotations.SerializedName

data class SaveDiaryRequest(
    val title: String,
    val content: String,
    val date: String,
    @SerializedName(value="deliveryYn")
    val deliveryYN: Char,
)