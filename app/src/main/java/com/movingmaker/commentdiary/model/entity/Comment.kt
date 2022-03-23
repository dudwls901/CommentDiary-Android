package com.movingmaker.commentdiary.model.entity

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName(value = "id")
    val id: Long,
    @SerializedName(value = "content")
    val content: String,
    @SerializedName(value ="date")
    val date: String,
    @SerializedName(value = "like")
    var like: Boolean
)
