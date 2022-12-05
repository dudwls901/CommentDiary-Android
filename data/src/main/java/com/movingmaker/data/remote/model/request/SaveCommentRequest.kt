package com.movingmaker.data.remote.model.request

import com.google.gson.annotations.SerializedName
import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.request.SaveCommentModel

data class SaveCommentRequest(
    @SerializedName(value = "diaryId")
    val id: Long,
    @SerializedName(value = "date")
    val date: String,
    @SerializedName(value = "content")
    val content: String,
) : RemoteModel {
    override fun toDomainModel() = SaveCommentModel(
        id, date, content
    )
}

fun SaveCommentModel.toDataModel() = SaveCommentRequest(
    id, date, content
)