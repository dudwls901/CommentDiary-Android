package com.movingmaker.data.remote.model.response

import com.google.gson.annotations.SerializedName
import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.response.ReceivedComment

data class ReceivedCommentResponse(
    @SerializedName(value = "commentId")
    val id: Long,
    @SerializedName(value = "content")
    val content: String,
    @SerializedName(value = "date")
    val date: String,
    @SerializedName(value = "like")
    val like: Boolean
) : RemoteModel {
    override fun toDomainModel() = ReceivedComment(
        id, content, date, like
    )
}

fun ReceivedComment.toDataModel() = ReceivedCommentResponse(
    id, content, date, like
)
