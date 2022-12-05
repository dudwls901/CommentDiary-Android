package com.movingmaker.data.remote.model.request

import com.google.gson.annotations.SerializedName
import com.movingmaker.data.remote.model.RemoteModel
import com.movingmaker.domain.model.request.ReportDiaryModel

data class ReportDiaryRequest(
    @SerializedName(value = "diaryId")
    val id: Long,
    @SerializedName(value = "content")
    val content: String,
) : RemoteModel {
    override fun toDomainModel() = ReportDiaryModel(
        id, content
    )
}

fun ReportDiaryModel.toDataModel() = ReportDiaryRequest(
    id, content
)