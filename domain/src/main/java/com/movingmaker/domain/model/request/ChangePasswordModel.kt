package com.movingmaker.domain.model.request

data class ChangePasswordModel(
    val password: String,
    val checkPassword: String
)