package com.movingmaker.data.remote.model.response

import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.response.ReceivedDiary
import kotlinx.serialization.Serializable

@Serializable
data class ReceivedDiaryResponse(
    val diaryId: Long,
    val title: String,
    val content: String,
    val date: String,
    val deliveryYn: Char,
    val myCommentResponse: List<ReceivedCommentResponse>
) : RemoteModel {
    override fun toDomainModel() = ReceivedDiary(
        diaryId,
        title,
        content,
        date,
        deliveryYn,
        myCommentResponse.map { it.toDomainModel() }.toMutableList()
    )
}

fun ReceivedDiary.toDataModel() = ReceivedDiaryResponse(
    id,
    title,
    content,
    date,
    deliveryYN,
    myComment.map { it.toDataModel() }.toMutableList()
)