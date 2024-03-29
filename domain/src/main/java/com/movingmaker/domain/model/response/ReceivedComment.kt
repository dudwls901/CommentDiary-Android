package com.movingmaker.domain.model.response

data class ReceivedComment(
    val commentId: Long,
    val content: String,
    val date: String,
    val like: Boolean
)
