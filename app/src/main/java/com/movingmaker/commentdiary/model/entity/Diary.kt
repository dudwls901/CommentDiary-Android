package com.movingmaker.commentdiary.model.entity

import com.google.gson.annotations.SerializedName

data class Diary(
    @SerializedName(value = "id")
    val id: Long?,
    @SerializedName(value = "title")
    var title: String,
    @SerializedName(value = "content")
    var content: String,
    @SerializedName(value = "date")
    var date: String,
    @SerializedName(value = "deliveryYn")
    var deliveryYN: Char,
    @SerializedName(value = "tempYn")
    var tempYN: Char,
    @SerializedName(value = "commentResponseList")
    val commentList: MutableList<Comment>?
)
