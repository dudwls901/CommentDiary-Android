package com.movingmaker.commentdiary.model.remote.request

data class SaveCommentRequest(
    val diaryId: Long,
    val date: String,
    val content: String,
)