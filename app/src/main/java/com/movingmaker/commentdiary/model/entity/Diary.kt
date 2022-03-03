package com.movingmaker.commentdiary.model.entity

import com.google.gson.annotations.SerializedName

data class Diary(
    @SerializedName(value = "id")
    val id: Long,
    @SerializedName(value = "title")
    val title: String,
    @SerializedName(value = "content")
    val content: String,
    @SerializedName(value = "date")
    val date: String,
    @SerializedName(value = "deliveryYN")
    val deliveryYN: String,
    @SerializedName(value = "commentResponseList")
    val commentList: MutableList<Comment>?
)
