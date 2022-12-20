package com.movingmaker.data.remote.model.response

import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.response.Diary
import kotlinx.serialization.Serializable

@Serializable
data class DiaryResponse(
    val id: Long,
    val title: String,
    val content: String,
    val date: String,
    val deliveryYn: Char,
    val commentResponseList: List<CommentResponse>
) : RemoteModel {
    override fun toDomainModel() = Diary(
        id = id,
        title = title,
        content = content,
        date = date,
        deliveryYN = deliveryYn,
        commentList = commentResponseList.map { it.toDomainModel() }.toMutableList()
    )
}

fun Diary.toDataModel() = DiaryResponse(
    id,
    title,
    content,
    date,
    deliveryYN,
    commentList.map { it.toDataModel() }.toMutableList()
)
