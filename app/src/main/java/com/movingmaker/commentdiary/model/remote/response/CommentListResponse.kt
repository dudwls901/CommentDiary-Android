package com.movingmaker.commentdiary.model.remote.response

import com.google.gson.annotations.SerializedName
import com.movingmaker.commentdiary.model.entity.Comment
import com.movingmaker.commentdiary.model.entity.Diary

data class CommentListResponse(
    @SerializedName(value = "code")
    val code: Int,
    @SerializedName(value = "message")
    val message: String,
    @SerializedName(value="result")
    val result: List<Comment>
)
