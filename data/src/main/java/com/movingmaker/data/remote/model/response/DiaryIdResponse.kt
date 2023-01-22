package com.movingmaker.data.remote.model.response

import com.movingmaker.data.remote.model.RemoteResponse
import com.movingmaker.domain.model.response.DiaryId
import kotlinx.serialization.Serializable

@Serializable
data class DiaryIdResponse(
    val id: Long
) : RemoteResponse {
    override fun toDomainModel() = DiaryId(
        id
    )
}
