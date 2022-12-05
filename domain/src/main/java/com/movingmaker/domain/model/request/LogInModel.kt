package com.movingmaker.domain.model.request

data class LogInModel(
    val email: String,
    val password: String,
    val deviceToken: String
)
