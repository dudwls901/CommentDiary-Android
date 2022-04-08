package com.movingmaker.commentdiary.model.remote.request

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    @SerializedName(value="email")
    val email: String,
    @SerializedName(value="password")
    val password: String,
    @SerializedName(value= "checkPassword")
    val checkPassword: String
)
