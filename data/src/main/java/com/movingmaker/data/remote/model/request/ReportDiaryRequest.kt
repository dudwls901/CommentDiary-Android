package com.movingmaker.data.remote.model.request

import com.movingmaker.domain.model.request.ReportDiaryModel
import kotlinx.serialization.Serializable

@Serializable
data class ReportDiaryRequest(
    val diaryId: Long,
    val content: String,
)

fun ReportDiaryModel.toDataModel() = ReportDiaryRequest(
    id, content
)