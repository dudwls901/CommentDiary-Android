package com.movingmaker.data.remote.model.response

import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.response.DiaryId
import kotlinx.serialization.Serializable

@Serializable
data class DiaryIdResponse(
    val id: Long
) : RemoteModel {
    override fun toDomainModel() = DiaryId(
        id
    )
}

fun DiaryId.toDataModel() = DiaryIdResponse(
    id
)
