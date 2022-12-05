package com.movingmaker.data.remote.model.request

import com.google.gson.annotations.SerializedName
import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.request.EditDiaryModel

data class EditDiaryRequest(
    @SerializedName(value = "title")
    val title: String,
    @SerializedName(value = "content")
    val content: String,
    @SerializedName(value = "tempYn")
    val tempYN: Char,
) : RemoteModel {
    override fun toDomainModel() = EditDiaryModel(
        title, content, tempYN
    )
}

fun EditDiaryModel.toDataModel() = EditDiaryRequest(
    title, content, tempYN
)