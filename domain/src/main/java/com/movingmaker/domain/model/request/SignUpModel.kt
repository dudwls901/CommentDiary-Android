package com.movingmaker.domain.model.request

data class SignUpModel(
    val email: String,
    val password: String,
    val checkPassword: String,
    val loginType: String,
    val isPushAgree: Boolean,
)
