package com.movingmaker.commentdiary.model.remote.request

import com.google.gson.annotations.SerializedName

data class ReportDiaryRequest(
    @SerializedName(value ="diaryId")
    val id: Long,
    val content: String,
)