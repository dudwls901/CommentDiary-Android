package com.movingmaker.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class SavedCommentResponse(
    val content: String,
    val date: String
)
