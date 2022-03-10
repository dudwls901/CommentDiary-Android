package com.movingmaker.commentdiary.model.remote.request

import com.google.gson.annotations.SerializedName

data class EditDiaryRequest(
    val title: String,
    val content: String,
)