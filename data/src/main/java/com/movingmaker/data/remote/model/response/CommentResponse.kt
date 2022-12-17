package com.movingmaker.data.remote.model.response

import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.response.Comment
import kotlinx.serialization.Serializable

@Serializable
data class CommentResponse(
    val id: Long,
    val content: String,
    val date: String,
    var like: Boolean
) : RemoteModel {
    override fun toDomainModel() = Comment(
        id, content, date, like
    )
}

fun Comment.toDataModel() = CommentResponse(
    id, content, date, like
)