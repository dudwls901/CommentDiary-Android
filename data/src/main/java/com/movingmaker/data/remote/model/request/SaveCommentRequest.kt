package com.movingmaker.data.remote.model.request

import com.movingmaker.domain.model.request.SaveCommentModel
import kotlinx.serialization.Serializable

@Serializable
data class SaveCommentRequest(
    val diaryId: Long,
    val date: String,
    val content: String,
)

fun SaveCommentModel.toDataModel() = SaveCommentRequest(
    id, date, content
)