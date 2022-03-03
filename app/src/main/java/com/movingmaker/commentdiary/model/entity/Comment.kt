package com.movingmaker.commentdiary.model.entity

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName(value = "id")
    val id: Int,
    @SerializedName(value = "content")
    val content: String,
    @SerializedName(value = "like")
    val like: Boolean
)
