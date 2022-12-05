package com.movingmaker.data.remote.model.request

import com.google.gson.annotations.SerializedName
import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.request.ReportCommentModel

data class ReportCommentRequest(
    @SerializedName(value = "commentId")
    val id: Long,
    @SerializedName(value = "content")
    val content: String,
) : RemoteModel {
    override fun toDomainModel() = ReportCommentModel(
        id, content
    )
}

fun ReportCommentModel.toDataModel() = ReportCommentRequest(
    id, content
)