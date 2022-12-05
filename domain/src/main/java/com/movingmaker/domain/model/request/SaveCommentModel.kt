package com.movingmaker.domain.model.request

data class SaveCommentModel(
    val id: Long,
    val date: String,
    val content: String,
)