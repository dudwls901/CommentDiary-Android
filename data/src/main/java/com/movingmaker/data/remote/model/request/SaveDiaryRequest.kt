package com.movingmaker.data.remote.model.request

import com.google.gson.annotations.SerializedName
import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.request.SaveDiaryModel

data class SaveDiaryRequest(
    @SerializedName(value = "title")
    val title: String,
    @SerializedName(value = "content")
    val content: String,
    @SerializedName(value = "date")
    val date: String,
    @SerializedName(value = "deliveryYn")
    val deliveryYN: Char,
    @SerializedName(value = "tempYn")
    val tempYN: Char,
) : RemoteModel {
    override fun toDomainModel() = SaveDiaryModel(
        title, content, date, deliveryYN, tempYN
    )
}

fun SaveDiaryModel.toDataModel() = SaveDiaryRequest(
    title, content, date, deliveryYN, tempYN
)