package com.movingmaker.commentdiary.data.remote.response

import com.google.gson.annotations.SerializedName
import com.movingmaker.commentdiary.data.model.Comment

data class CommentListResponse(
    @SerializedName(value = "code")
    val code: Int,
    @SerializedName(value = "message")
    val message: String,
    @SerializedName(value="result")
    val result: List<Comment>
)
