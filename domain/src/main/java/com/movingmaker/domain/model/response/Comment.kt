package com.movingmaker.domain.model.response

data class Comment(
    val id: Long,
    val content: String,
    val date: String,
    val like: Boolean
)
