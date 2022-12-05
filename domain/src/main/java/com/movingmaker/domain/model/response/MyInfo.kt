package com.movingmaker.domain.model.response

data class MyInfo(
    val email: String,
    val loginType: String,
    val temperature: Double,
    val pushYN: Char
)
