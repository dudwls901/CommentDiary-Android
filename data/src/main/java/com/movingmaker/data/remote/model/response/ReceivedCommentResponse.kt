package com.movingmaker.data.remote.model.response

import com.movingmaker.data.remote.model.RemoteResponse
import com.movingmaker.domain.model.response.ReceivedComment
import kotlinx.serialization.Serializable

@Serializable
data class ReceivedCommentResponse(
    val commentId: Long,
    val content: String,
    val date: String,
    val like: Boolean
) : RemoteResponse {
    override fun toDomainModel() = ReceivedComment(
        commentId, content, date, like
    )
}