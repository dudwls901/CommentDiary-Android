package com.movingmaker.data.remote.model.response

import com.google.gson.annotations.SerializedName
import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.response.ReceivedDiary

data class ReceivedDiaryResponse(
    @SerializedName(value = "diaryId")
    val id: Long?,
    @SerializedName(value = "title")
    var title: String,
    @SerializedName(value = "content")
    var content: String,
    @SerializedName(value = "date")
    var date: String,
    @SerializedName(value = "deliveryYn")
    var deliveryYN: Char,
    @SerializedName(value = "tempYn")
    var tempYN: Char,
    @SerializedName(value = "myCommentResponse")
    val myComment: MutableList<ReceivedCommentResponse>?
) : RemoteModel {
    override fun toDomainModel() = ReceivedDiary(
        id,
        title,
        content,
        date,
        deliveryYN,
        tempYN,
        myComment?.map { it.toDomainModel() }?.toMutableList()
    )
}

fun ReceivedDiary.toDataModel() = ReceivedDiaryResponse(
    id,
    title,
    content,
    date,
    deliveryYN,
    tempYN,
    myComment?.map { it.toDataModel() }?.toMutableList()
)