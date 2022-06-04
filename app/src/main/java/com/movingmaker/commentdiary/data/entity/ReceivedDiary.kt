package com.movingmaker.commentdiary.data.entity

import com.google.gson.annotations.SerializedName

data class ReceivedDiary(
    @SerializedName(value = "diaryId")
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
    @SerializedName(value = "myCommentResponse")
    val myComment: MutableList<ReceivedComment>?
)
