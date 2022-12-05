package com.movingmaker.data.remote.model.response

import com.google.gson.annotations.SerializedName
import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.response.Diary

data class DiaryResponse(
    @SerializedName(value = "id")
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
    @SerializedName(value = "commentResponseList")
    val commentList: MutableList<CommentResponse>?
) : RemoteModel {
    override fun toDomainModel() = Diary(
        id,
        title,
        content,
        date,
        deliveryYN,
        tempYN,
        commentList?.map { it.toDomainModel() }?.toMutableList()
    )
}

fun Diary.toDataModel() = DiaryResponse(
    id,
    title,
    content,
    date,
    deliveryYN,
    tempYN,
    commentList?.map { it.toDataModel() }?.toMutableList()
)
