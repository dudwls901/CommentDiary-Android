package com.movingmaker.commentdiary.model.entity

import com.google.gson.annotations.SerializedName

data class DiaryId(
    @SerializedName(value = "id")
    val id: Long
)
