package com.movingmaker.commentdiary.data.remote.request

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @SerializedName(value= "password")
    val password: String,
    @SerializedName(value= "checkPassword")
    val checkPassword: String
)
