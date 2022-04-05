package com.movingmaker.commentdiary.model.remote.response

import com.google.gson.annotations.SerializedName
import com.movingmaker.commentdiary.model.entity.Comment

data class CommentPushStateResponse(
    val code: Int,
    val message: String,
    @SerializedName(value="result")
    val result: Map<String,Char>
)
