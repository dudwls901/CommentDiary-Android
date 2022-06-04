package com.movingmaker.commentdiary.data.entity

import com.google.gson.annotations.SerializedName

data class MyInfo(
    @SerializedName(value = "email")
    val email: String,
    @SerializedName(value = "temperature")
    val temperature: Double,
    @SerializedName(value = "pushYn")
    val pushYN: Char
)
