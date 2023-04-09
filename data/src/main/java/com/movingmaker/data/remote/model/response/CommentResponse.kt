package com.movingmaker.data.remote.model.response

import com.movingmaker.data.remote.model.RemoteResponse
import com.movingmaker.domain.model.response.Comment
import kotlinx.serialization.Serializable

@Serializable
data class CommentResponse(
    val id: Long,
    val content: String,
    val date: String,
    val like: Boolean
) : RemoteResponse {
    override fun toDomainModel() = Comment(
        id, content, date, like
    )
}