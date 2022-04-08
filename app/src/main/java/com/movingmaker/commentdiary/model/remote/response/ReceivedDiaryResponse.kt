package com.movingmaker.commentdiary.model.remote.response

import com.google.gson.annotations.SerializedName
import com.movingmaker.commentdiary.model.entity.Diary
import com.movingmaker.commentdiary.model.entity.ReceivedDiary

data class ReceivedDiaryResponse(
    @SerializedName(value = "code")
    val code: Int,
    @SerializedName(value = "message")
    val message: String,
    @SerializedName(value="result")
    val result: ReceivedDiary
)
