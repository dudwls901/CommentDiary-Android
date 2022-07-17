package com.movingmaker.commentdiary.data.remote.request

import com.google.gson.annotations.SerializedName

data class KakaoSignUpRequest(
    @SerializedName("pushYn")
    val pushYN: Char
)
