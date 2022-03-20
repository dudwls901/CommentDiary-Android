package com.movingmaker.commentdiary.model.remote.response

import com.google.gson.annotations.SerializedName
import com.movingmaker.commentdiary.model.entity.Diary
import com.movingmaker.commentdiary.model.entity.ReceivedDiary

data class ReceivedDiaryResponse(
    val code: Int,
    val message: String,
    @SerializedName(value="result")
    val result: ReceivedDiary
)
