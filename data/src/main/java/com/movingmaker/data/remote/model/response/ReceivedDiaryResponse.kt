package com.movingmaker.data.remote.model.response

import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.response.ReceivedDiary
import kotlinx.serialization.Serializable

@Serializable
data class ReceivedDiaryResponse(
    val diaryId: Long?,
    var title: String,
    var content: String,
    var date: String,
    var deliveryYn: Char,
    var tempYn: Char,
    val myCommentResponse: MutableList<ReceivedCommentResponse>?
) : RemoteModel {
    override fun toDomainModel() = ReceivedDiary(
        diaryId,
        title,
        content,
        date,
        deliveryYn,
        tempYn,
        myCommentResponse?.map { it.toDomainModel() }?.toMutableList()
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