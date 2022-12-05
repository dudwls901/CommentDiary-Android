package com.movingmaker.domain.model.request

data class KakaoLoginModel(
    val loginType: String,
    val accessToken: String,
    val deviceToken: String
)