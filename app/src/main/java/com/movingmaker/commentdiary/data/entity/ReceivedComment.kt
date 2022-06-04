package com.movingmaker.commentdiary.data.entity

import com.google.gson.annotations.SerializedName

data class ReceivedComment(
    @SerializedName(value = "commentId")
    val id: Long,
    @SerializedName(value = "content")
    val content: String,
    @SerializedName(value ="date")
    val date: String,
    @SerializedName(value = "like")
    val like: Boolean
)
