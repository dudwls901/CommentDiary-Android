package com.movingmaker.data.remote.model.response

import com.google.gson.annotations.SerializedName
import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.response.DiaryId

data class DiaryIdResponse(
    @SerializedName(value = "id")
    val id: Long
) : RemoteModel {
    override fun toDomainModel() = DiaryId(
        id
    )
}

fun DiaryId.toDataModel() = DiaryIdResponse(
    id
)
