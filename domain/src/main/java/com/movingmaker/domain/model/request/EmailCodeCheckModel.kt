package com.movingmaker.domain.model.request

data class EmailCodeCheckModel(
    val email: String,
    val code: Int
)