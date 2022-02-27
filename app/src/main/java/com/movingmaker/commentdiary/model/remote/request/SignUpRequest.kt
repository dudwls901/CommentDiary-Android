package com.movingmaker.commentdiary.model.remote.request

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    val email: String,
    val password: String,
    @SerializedName(value= "checkPassword")
    val checkPassword: String
)
