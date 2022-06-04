package com.movingmaker.commentdiary.data.remote.request

import com.google.gson.annotations.SerializedName

data class ReportCommentRequest(
    @SerializedName(value ="commentId")
    val id: Long,
    @SerializedName(value="content")
    val content: String,
)