package com.movingmaker.commentdiary.model.remote.request

import com.google.gson.annotations.SerializedName

data class SaveCommentRequest(
    @SerializedName(value = "diaryId")
    val id: Long,
    @SerializedName(value="date")
    val date: String,
    @SerializedName(value="content")
    val content: String,
)