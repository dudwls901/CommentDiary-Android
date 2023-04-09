package com.movingmaker.data.remote.model.response

import com.movingmaker.data.remote.model.RemoteResponse
import com.movingmaker.domain.model.response.ReceivedDiary
import kotlinx.serialization.Serializable

@Serializable
data class ReceivedDiaryResponse(
    val diaryId: Long,
    val title: String,
    val content: String,
    val date: String,
    val myCommentResponse: List<ReceivedCommentResponse>
) : RemoteResponse {
    override fun toDomainModel() = ReceivedDiary(
        diaryId,
        title,
        content,
        date,
        myCommentResponse.map { it.toDomainModel() }.toMutableList()
    )
}