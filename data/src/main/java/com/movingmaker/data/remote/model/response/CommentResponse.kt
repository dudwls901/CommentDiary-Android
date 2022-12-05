package com.movingmaker.data.remote.model.response

import com.google.gson.annotations.SerializedName
import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.response.Comment

data class CommentResponse(
    @SerializedName(value = "id")
    val id: Long,
    @SerializedName(value = "content")
    val content: String,
    @SerializedName(value = "date")
    val date: String,
    @SerializedName(value = "like")
    var like: Boolean
) : RemoteModel {
    override fun toDomainModel() = Comment(
        id, content, date, like
    )
}

fun Comment.toDataModel() = CommentResponse(
    id, content, date, like
)