package com.movingmaker.data.remote.model.response

import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.response.ReceivedComment
import kotlinx.serialization.Serializable

@Serializable
data class ReceivedCommentResponse(
    val commentId: Long,
    val content: String,
    val date: String,
    val like: Boolean
) : RemoteModel {
    override fun toDomainModel() = ReceivedComment(
        commentId, content, date, like
    )
}

fun ReceivedComment.toDataModel() = ReceivedCommentResponse(
    id, content, date, like
)
