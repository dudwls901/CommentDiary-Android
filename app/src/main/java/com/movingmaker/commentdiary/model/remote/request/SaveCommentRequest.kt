package com.movingmaker.commentdiary.model.remote.request

import com.google.gson.annotations.SerializedName

data class SaveCommentRequest(
    @SerializedName(value = "diaryId")
    val id: Long,
    val date: String,
    val content: String,
)