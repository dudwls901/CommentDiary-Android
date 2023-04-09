package com.movingmaker.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val id: Long,
    val content: String,
    val date: String,
    val like: Boolean
)
