package com.movingmaker.data.remote.model.request

import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.request.ReportCommentModel
import kotlinx.serialization.Serializable

@Serializable
data class ReportCommentRequest(
    val commentId: Long,
    val content: String,
) : RemoteModel {
    override fun toDomainModel() = ReportCommentModel(
        commentId, content
    )
}

fun ReportCommentModel.toDataModel() = ReportCommentRequest(
    id, content
)