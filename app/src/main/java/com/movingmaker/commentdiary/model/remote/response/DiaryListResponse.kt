package com.movingmaker.commentdiary.model.remote.response

import com.google.gson.annotations.SerializedName
import com.movingmaker.commentdiary.model.entity.Diary

data class DiaryListResponse(
    val code: Int,
    val message: String,
    @SerializedName(value="result")
    val result: MutableList<Diary>
)
